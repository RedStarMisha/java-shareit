package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.CommentCreationException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("repository")
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpWithRepository implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final RequestStorage requestStorage;

    @Lazy
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;


    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest = requestStorage.getRequest(userId, itemDto.getRequest()).orElse(null);
        Item item = itemRepository.save(Mapper.toEntity(owner, itemDto, itemRequest));
        log.info(item.toString());
        return Mapper.toDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        return itemRepository.findByOwner_IdAndId(userId, itemId).map(item -> {
            Item item1 = Mapper.updateFromDto(item, itemDto);
            itemRepository.save(item1);
            return Mapper.toDto(item1);
        }).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemForResponse getItemById(long userId, long itemId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return itemRepository.findById(itemId).map(item -> Mapper.toResponseEntity(item, getLastBooking(itemId),
                        getNextBooking(itemId), getComments(itemId))).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemForResponse> getUserItems(long userId) {
        return itemRepository.findAllByOwner_Id(userId).stream().map(item ->
            Mapper.toResponseEntity(item, getLastBooking(item.getId()), getNextBooking(item.getId()),
                    getComments(item.getId()))).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByName(String text) {
        return itemRepository.search(text).stream().map(Mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        Booking booking = bookingRepository.findFirstByItem_IdAndBooker_IdAndEndBefore(itemId, authorId,
                LocalDateTime.now()).orElseThrow(() -> new CommentCreationException(String.format("User с id = %d" +
                " не может оставить комментарий Item c id = %d", authorId, itemId)));
        Comment comment = Mapper.toEntity(author, item, commentDto);
        log.info("{} добавлен", comment);
        return Mapper.toDto(commentRepository.save(comment));
    }

    private BookingShort getLastBooking(long itemId) {
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndEndBeforeOrderByEndAsc(itemId, LocalDateTime.now())
                .orElse(null);
        return Mapper.toBookingShort(lastBooking);
    }

    private BookingShort getNextBooking(long itemId) {
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndStartAfterOrderByStartDesc(itemId, LocalDateTime.now())
                .orElse(null);
        return Mapper.toBookingShort(nextBooking);
    }

    private List<CommentDto> getComments(long itemId) {
        return commentRepository.findAllByItem_Id(itemId).stream().map(Mapper::toDto).collect(Collectors.toList());
    }
}

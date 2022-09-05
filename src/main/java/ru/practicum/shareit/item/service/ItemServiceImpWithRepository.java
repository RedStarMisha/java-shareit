package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.CommentCreationException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.*;

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
    public ItemDtoEntry addItem(long userId, ItemDtoEntry itemDtoEntry) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest = requestStorage.getRequest(userId, itemDtoEntry.getRequest()).orElse(null);
        Item item = itemRepository.save(toItem(owner, itemDtoEntry, itemRequest));
        log.info(item.toString());
        return toCommentDto(item);
    }

    @Override
    public ItemDtoEntry updateItem(long userId, long itemId, ItemDtoEntry itemDtoEntry) {
        return itemRepository.findByOwner_IdAndId(userId, itemId).map(item -> {
            Item item1 = updateFromDto(item, itemDtoEntry);
            itemRepository.save(item1);
            return toCommentDto(item1);
        }).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemForResponse getItemById(long userId, long itemId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        return toResponseItem(item, getLastBooking(itemId, userId),
                getNextBooking(itemId, userId), getComments(itemId));
    }

    @Override
    public List<ItemForResponse> getUserItems(long userId) {
        List<Item> items = itemRepository.findAllByOwner_Id(userId);
        return items.stream().map(i -> toResponseItem(i, getLastBooking(i.getId(), i.getOwner().getId()),
                getNextBooking(i.getId(), i.getOwner().getId()), getComments(i.getId()))).collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoEntry> findItemByName(String text) {
        return itemRepository.search(text).stream().map(ItemMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        Booking booking = bookingRepository.findFirstByItem_IdAndBooker_IdAndEndBefore(itemId, authorId,
                LocalDateTime.now()).orElseThrow(() -> new CommentCreationException(String.format("User с id = %d" +
                " не может оставить комментарий Item c id = %d", authorId, itemId)));
        Comment comment = toComment(author, item, commentDto);
        log.info("{} добавлен", comment);
        return toCommentDto(commentRepository.save(comment));
    }

    private BookingShort getLastBooking(long itemId, long ownerId) {
        Booking lastBooking = bookingRepository.findFirstByItem_IdAndItem_Owner_IdAndEndBeforeOrderByEndAsc(itemId,
                ownerId, LocalDateTime.now()).orElse(null);
        return BookingMapper.toBookingShort(lastBooking);
    }

    private BookingShort getNextBooking(long itemId, long ownerId) {
        Booking nextBooking = bookingRepository.findFirstByItem_IdAndItem_Owner_IdAndStartAfterOrderByStartDesc(itemId,
                ownerId, LocalDateTime.now()).orElse(null);
        return BookingMapper.toBookingShort(nextBooking);
    }

    private List<CommentDto> getComments(long itemId) {
        return commentRepository.findAllByItem_Id(itemId).stream().map(ItemMapper::toCommentDto).collect(Collectors.toList());
    }
}

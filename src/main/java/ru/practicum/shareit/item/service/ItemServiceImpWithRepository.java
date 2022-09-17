package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.CommentCreationException;
import ru.practicum.shareit.exceptions.PaginationParametersException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestRepository;
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
@Transactional
public class ItemServiceImpWithRepository implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;

    @Lazy
    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public ItemDtoShort addItem(long userId, ItemDtoShort itemDtoShort) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest;
        if (itemDtoShort.getRequestId() != null) {
            itemRequest = requestRepository.findById(itemDtoShort.getRequestId())
                    .orElseThrow(() -> new RequestNotFoundException(itemDtoShort.getRequestId()));

        } else {
            itemRequest = null;
        }
        Item item = itemRepository.save(toItem(owner, itemDtoShort, itemRequest));
        log.info(item.toString());
        return toItemDto(item);
    }

    @Override
    @Transactional
    public ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort itemDtoShort) {
        return itemRepository.findByOwner_IdAndId(userId, itemId).map(item -> {
            Item item1 = updateFromDto(item, itemDtoShort);
            itemRepository.save(item1);
            return toItemDto(item1);
        }).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        return toResponseItem(item, getLastBooking(itemId, userId),
                getNextBooking(itemId, userId), getComments(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId, int from, int size) {
        List<Item> items = itemRepository.findAllByOwner_IdOrderById(userId, makePageParam(from, size));
        return items.stream().map(i -> toResponseItem(i, getLastBooking(i.getId(), i.getOwner().getId()),
                getNextBooking(i.getId(), i.getOwner().getId()), getComments(i.getId()))).collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoShort> findItemByName(String text, int from, int size) {
        return itemRepository.search(text, makePageParam(from, size)).stream().map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException(authorId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException(itemId));
        bookingRepository.findFirstByItem_IdAndBooker_IdAndEndBefore(itemId, authorId, LocalDateTime.now()).orElseThrow(() ->
                new CommentCreationException(String.format("User с id = %d" +
                        " не может оставить комментарий Item c id = %d", authorId, itemId)));
        Comment comment = toComment(author, item, commentDto);
        log.info("{} добавлен", comment);
        return toCommentDto(commentRepository.save(comment));
    }

    private BookingShort getLastBooking(long itemId, long ownerId) {
        return bookingRepository.findFirstByItem_IdAndItem_Owner_IdAndEndBeforeOrderByEndAsc(itemId, ownerId,
                LocalDateTime.now()).map(BookingMapper::toBookingShort).orElse(null);
    }

    private BookingShort getNextBooking(long itemId, long ownerId) {
        return bookingRepository.findFirstByItem_IdAndItem_Owner_IdAndStartAfterOrderByStartDesc(itemId, ownerId,
                LocalDateTime.now()).map(BookingMapper::toBookingShort).orElse(null);
    }

    private List<CommentDto> getComments(long itemId) {
        return commentRepository.findAllByItem_Id(itemId).stream().map(ItemMapper::toCommentDto).collect(Collectors.toList());
    }

    public static Pageable makePageParam(int from, int size) {
        if (from < 0 || size < 1) {
            throw new PaginationParametersException("Неверные параметры страницы");
        }
        int page = from / size;
        Sort sort = Sort.by("id").ascending();
        return PageRequest.of(page, size, sort);
    }
}

package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.*;
import static ru.practicum.shareit.user.UserMapper.toEntity;

@Service
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final RequestStorage requestStorage;
    private long idCounter = 1;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, @Qualifier("storage") UserService userService,
                           RequestStorage requestStorage) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.requestStorage = requestStorage;
    }

    @Override
    public ItemDtoShort addItem(Long userId, ItemDtoShort itemDtoShort) {
        final User user = toEntity(userService.getUserById(userId));
        itemDtoShort.setId(idCounter++);
        ItemRequest itemRequest;
        if (itemDtoShort.getRequestId() != null) {
            itemRequest = requestStorage.getRequest(itemDtoShort.getRequestId())
                    .orElseThrow(()-> new RequestNotFoundException(itemDtoShort.getRequestId()));
        } else {
            itemRequest = null;
        }
        return toItemDto(itemStorage.addItem(userId, toItem(user, itemDtoShort, itemRequest)));
    }

    @Override
    public ItemDto getItemById(Long userId, long itemId) {
        userService.getUserById(userId);
        return itemStorage.getItemById(userId, itemId).map(item -> toResponseItem(item, null, null, null))
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort itemDtoShort) {
        userService.getUserById(userId);
        itemDtoShort.setId(itemId);
        ItemRequest itemRequest;
        if (itemDtoShort.getRequestId() != null) {
            itemRequest = requestStorage.getRequest(itemDtoShort.getRequestId())
                    .orElseThrow(()-> new RequestNotFoundException(itemDtoShort.getRequestId()));
        } else {
            itemRequest = null;
        }
        return itemStorage.updateItem(userId, toItem(null, itemDtoShort, itemRequest)).map(ItemMapper::toItemDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId, int from, int size) {
        userService.getUserById(userId);
        return itemStorage.getUserItems(userId).stream().map(item ->
                toResponseItem(item, null, null, null)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoShort> findItemByName(String text, int from, int size) {
        return itemStorage.findItemByName(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        return null;
    }

}

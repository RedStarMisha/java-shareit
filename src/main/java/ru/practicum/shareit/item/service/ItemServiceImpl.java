package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.*;
import static ru.practicum.shareit.requests.RequestMapper.toRequest;
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
    public ItemDtoEntry addItem(long userId, ItemDtoEntry itemDtoEntry) {
        final User user = toEntity(userService.getUserById(userId));
        itemDtoEntry.setId(idCounter++);
        ItemRequest itemRequest;
        if (itemDtoEntry.getRequestId() != null) {
            itemRequest = requestStorage.getRequest(itemDtoEntry.getRequestId())
                    .orElseThrow(()-> new RequestNotFoundException(itemDtoEntry.getRequestId()));
        } else {
            itemRequest = null;
        }
        return toItemDto(itemStorage.addItem(userId, toItem(user, itemDtoEntry, itemRequest)));
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        userService.getUserById(userId);
        return itemStorage.getItemById(userId, itemId).map(item -> toResponseItem(item, null, null, null))
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDtoEntry updateItem(long userId, long itemId, ItemDtoEntry itemDtoEntry) {
        userService.getUserById(userId);
        itemDtoEntry.setId(itemId);
        ItemRequest itemRequest;
        if (itemDtoEntry.getRequestId() != null) {
            itemRequest = requestStorage.getRequest(itemDtoEntry.getRequestId())
                    .orElseThrow(()-> new RequestNotFoundException(itemDtoEntry.getRequestId()));
        } else {
            itemRequest = null;
        }
        return itemStorage.updateItem(userId, toItem(null, itemDtoEntry, itemRequest)).map(ItemMapper::toItemDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        userService.getUserById(userId);
        return itemStorage.getUserItems(userId).stream().map(item ->
                toResponseItem(item, null, null, null)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoEntry> findItemByName(String text) {
        return itemStorage.findItemByName(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        return null;
    }

}

package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
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
    private final RequestService requestService;
    private long idCounter = 1;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage, @Qualifier("storage") UserService userService,
                           @Qualifier("repository") RequestService requestService) {
        this.itemStorage = itemStorage;
        this.userService = userService;
        this.requestService = requestService;
    }

    @Override
    public ItemDtoEntry addItem(long userId, ItemDtoEntry itemDtoEntry) {
        final User user = toEntity(userService.getUserById(userId));
        itemDtoEntry.setId(idCounter++);
        final ItemRequest itemRequest = itemDtoEntry.getRequest() != null ?
                toRequest(userId, requestService.getRequest(userId, itemDtoEntry.getRequest())) : null;
        return toCommentDto(itemStorage.addItem(userId, toItem(user, itemDtoEntry, itemRequest)));
    }

    @Override
    public ItemForResponse getItemById(long userId, long itemId) {
        userService.getUserById(userId);
        return itemStorage.getItemById(userId, itemId).map(item -> toResponseItem(item, null, null, null))
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDtoEntry updateItem(long userId, long itemId, ItemDtoEntry itemDtoEntry) {
        userService.getUserById(userId);
        itemDtoEntry.setId(itemId);
        final ItemRequest itemRequest = itemDtoEntry.getRequest() != null ?
                toRequest(userId, requestService.getRequest(userId, itemDtoEntry.getRequest())) : null;
        return itemStorage.updateItem(userId, toItem(null, itemDtoEntry, itemRequest)).map(ItemMapper::toCommentDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemForResponse> getUserItems(long userId) {
        userService.getUserById(userId);
        return itemStorage.getUserItems(userId).stream().map(item ->
                toResponseItem(item, null, null, null)).collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoEntry> findItemByName(String text) {
        return itemStorage.findItemByName(text).stream().map(ItemMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        return null;
    }

}

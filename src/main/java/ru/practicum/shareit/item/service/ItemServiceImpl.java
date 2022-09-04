package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.requests.RequestMapper.toRequest;
import static ru.practicum.shareit.Mapper.toEntity;

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
    public ItemDto addItem(long userId, ItemDto itemDto) {
        final User user = toEntity(userService.getUserById(userId));
        itemDto.setId(idCounter++);
        final ItemRequest itemRequest = itemDto.getRequest() != null ?
                toRequest(userId, requestService.getRequest(userId, itemDto.getRequest())) : null;
        return toItemDto(itemStorage.addItem(userId, toItem(user, itemDto, itemRequest)));
    }

    @Override
    public ItemForResponse getItemById(long userId, long itemId) {
        userService.getUserById(userId);
        return itemStorage.getItemById(userId, itemId).map(item -> Mapper.toResponseEntity(item, null, null, null))
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userService.getUserById(userId);
        itemDto.setId(itemId);
        final ItemRequest itemRequest = itemDto.getRequest() != null ?
                toRequest(userId, requestService.getRequest(userId, itemDto.getRequest())) : null;
        return itemStorage.updateItem(userId, toItem(null, itemDto, itemRequest))
                .map(ItemMapper::toItemDto).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemForResponse> getUserItems(long userId) {
        userService.getUserById(userId);
        return itemStorage.getUserItems(userId).stream().map( item ->
                Mapper.toResponseEntity(item, null, null, null)
        ).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByName(String text) {
        return itemStorage.findItemByName(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(long authorId, long itemId, CommentDto commentDto) {
        return null;
    }

}

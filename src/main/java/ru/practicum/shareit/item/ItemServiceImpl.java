package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.requests.RequestMapper;
import ru.practicum.shareit.requests.RequestService;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.item.ItemMapper.toItem;
import static ru.practicum.shareit.item.ItemMapper.toItemDto;
import static ru.practicum.shareit.requests.RequestMapper.toRequest;
import static ru.practicum.shareit.user.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final RequestService requestService;
    private long idCounter = 1;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        final User user = toUser(userService.getUserById(userId));
        itemDto.setId(idCounter++);
        final ItemRequest itemRequest = itemDto.getRequest() != null ?
                toRequest(userId, requestService.getRequest(userId, itemDto.getRequest())) : null;
        return toItemDto(itemStorage.addItem(userId, toItem(user, itemDto, itemRequest)));
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        userService.getUserById(userId);
        return itemStorage.getItemById(userId, itemId).map(ItemMapper::toItemDto)
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
    public List<ItemDto> getUserItems(long userId) {
        userService.getUserById(userId);
        return itemStorage.getUserItems(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByName(String text) {
        return itemStorage.findItemByName(text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

}

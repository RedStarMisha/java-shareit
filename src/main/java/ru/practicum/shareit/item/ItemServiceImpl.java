package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemStorage itemStorage;

    private final UserService userService;

    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) throws UserNotFoundException {
        userService.getUserById(userId);
        return ItemMapper.convertToDto(itemStorage.addItem(userId, itemDto));
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) throws UserNotFoundException, ItemNotFoundException {
        userService.getUserById(userId);
        return itemStorage.getItemById(userId, itemId).map(ItemMapper::convertToDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws UserNotFoundException,
            ItemNotFoundException {
        userService.getUserById(userId);
        return itemStorage.updateItem(userId, itemId, itemDto).map(ItemMapper::convertToDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) throws UserNotFoundException {
        userService.getUserById(userId);
        return itemStorage.getUserItems(userId).stream().map(ItemMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByName(String text) {
        return itemStorage.findItemByName(text).stream().map(ItemMapper::convertToDto).collect(Collectors.toList());
    }

}

package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto) throws UserNotFoundException;

    ItemDto getItemById(long userId, long itemId) throws UserNotFoundException, ItemNotFoundException;

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto) throws UserNotFoundException, ItemNotFoundException;

    List<ItemDto> getUserItems(long userId) throws UserNotFoundException;

    List<ItemDto> findItemByName(String text);
}

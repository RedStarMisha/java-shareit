package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemDto getItemById(long userId, long itemId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    List<ItemDto> getUserItems(long userId);

    List<ItemDto> findItemByName(String text);
}

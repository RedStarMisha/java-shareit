package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    /**
     *
     * @param userId - id of the user who adds the item
     * @param itemDto - dto of Item that will be added
     * @return  Item that added into storage
     */
    Item addItem(long userId, ItemDto itemDto);

    Optional<Item> getItemById(long userId, long itemId);

    Optional<Item> updateItem(long userId, long itemId, ItemDto itemDto);

    List<Item> getUserItems(long userId);

    List<Item> findItemByName(String text);
}

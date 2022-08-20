package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto convertToDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(), item.getRequest());
    }

    public static Item convertFromDto(long id, long owner, ItemDto itemDto) {
        Item item = new Item();
        item.setId(id);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(owner);
        item.setRequest(itemDto.getRequest());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}

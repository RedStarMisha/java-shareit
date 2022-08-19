package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.requests.ItemRequest;

/**
 * // Item class in storage .
 */
@Data
public class Item {
    private long id;
    private String name;
    private String description;
    private long owner;
    private boolean available;
    private ItemRequest request;

    public Item(long id, long owner, ItemDto itemDto) {
        this.id = id;
        this.name = itemDto.getName();
        this.description = itemDto.getDescription();
        this.owner = owner;
        this.available = itemDto.getAvailable();
    }
}

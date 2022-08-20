package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.model.ItemRequest;

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
}

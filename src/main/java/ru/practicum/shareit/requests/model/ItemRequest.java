package ru.practicum.shareit.requests.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * // Item Request model .
 */
@Data
public class ItemRequest {
    private long id;

    private String description;
    private long requestor;
    private LocalDateTime created;

    public ItemRequest(long id, long userId, ItemRequestDto itemRequestDto) {
        this.id = id;
        requestor = userId;
        description = itemRequestDto.getDescription();
        created = LocalDateTime.now();
    }
}

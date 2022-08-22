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
}

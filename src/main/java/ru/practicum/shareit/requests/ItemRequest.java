package ru.practicum.shareit.requests;

import lombok.Data;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.time.LocalDateTime;

/**
 * // TODO .
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

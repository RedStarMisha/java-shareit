package ru.practicum.shareit.item.dto;

import lombok.Data;

/**
 * // TODO .
 */
@Data
public class ItemDtoShort {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;
}

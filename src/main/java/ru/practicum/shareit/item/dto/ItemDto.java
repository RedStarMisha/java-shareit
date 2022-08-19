package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    @NotNull @NotBlank
    private String name;
    @NotNull @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private ItemRequest request;
}

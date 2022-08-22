package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.IsText;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    @IsText(groups = Update.class)
    private String name;
    @NotBlank(groups = {Create.class})
    @IsText(groups = Update.class)
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long request;
}

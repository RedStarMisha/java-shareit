package ru.practicum.shareit.controller.item;

import lombok.Value;
import validation.Create;
import validation.IsText;
import validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Value
public class ItemDtoShort {

    private Long id;

    @NotBlank(groups = {Create.class})
    @IsText(groups = Update.class)
    private String name;

    @NotBlank(groups = {Create.class})
    @IsText(groups = Update.class)
    private String description;

    @NotNull(groups = {Create.class})
    private Boolean available;

    private Long requestId;
}

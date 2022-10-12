package ru.practicum.shareit.controller.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * // Item Request Dto .
 */
@Data
public class ItemRequestDtoEntry {

    @NotBlank
    private String description;
}

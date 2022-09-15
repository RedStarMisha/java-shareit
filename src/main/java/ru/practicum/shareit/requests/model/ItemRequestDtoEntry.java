package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * // Item Request Dto .
 */
@Data
@AllArgsConstructor
public class ItemRequestDtoEntry {
    @NotBlank
    private String description;
}

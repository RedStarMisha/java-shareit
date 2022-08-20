package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * // Item Request Dto .
 */
@Data
@AllArgsConstructor
public class ItemRequestDto {
    @NotNull
    @NotBlank
    private String description;
}

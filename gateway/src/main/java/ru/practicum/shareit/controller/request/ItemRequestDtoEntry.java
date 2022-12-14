package ru.practicum.shareit.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * // Item Request Dto .
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDtoEntry {

    @NotBlank
    private String description;
}

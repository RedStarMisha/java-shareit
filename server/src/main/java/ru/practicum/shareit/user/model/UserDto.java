package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UserDto class
 */
@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String email;
}

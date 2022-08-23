package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * // User class in storage .
 */
@Data
@AllArgsConstructor
public class User {
    private long id;
    private String name;
    private String email;
}

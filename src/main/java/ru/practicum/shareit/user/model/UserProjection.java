package ru.practicum.shareit.user.model;


import lombok.Data;

@Data
public class UserProjection {
    private long id;

    private String name;

    private String email;
}

package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUserById(long userId);

    UserDto getUserById(long userId);

    List<UserDto> getAllUSer();
}

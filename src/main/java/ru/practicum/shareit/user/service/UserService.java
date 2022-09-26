package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    void deleteUserById(Long userId);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUSer();
}

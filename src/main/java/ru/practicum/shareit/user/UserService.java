package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto user) throws EmailAlreadyExistException;

    UserDto updateUser(long userId, UserDto user) throws UserNotFoundException, EmailAlreadyExistException;

    void deleteUserById(long id) throws UserNotFoundException;

    UserDto getUserById(long id) throws UserNotFoundException;

    List<UserDto> getAllUSer();
}

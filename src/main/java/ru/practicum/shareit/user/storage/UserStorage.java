package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(UserDto user);

    Optional<User> updateUser(long userId, UserDto user);

    void deleteUserById(long id);

    Optional<User> getUserById(long id);

    List<User> getAllUser();

    boolean emailExistingCheck(String email);

}

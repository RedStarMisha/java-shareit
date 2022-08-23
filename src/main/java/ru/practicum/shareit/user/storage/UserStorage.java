package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User addUser(User user);

    Optional<User> updateUser(User user);

    void deleteUserById(long id);

    Optional<User> getUserById(long id);

    List<User> getAllUser();

    boolean emailExistingCheck(String email);

}

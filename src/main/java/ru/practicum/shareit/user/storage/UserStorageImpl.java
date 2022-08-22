package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * User storage into application memory
 */

@Slf4j
@Repository
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();

    @Override
    public User addUser(User user) {
        storage.put(user.getId(), user);
        log.info(user + " добавлен");
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        if (!storage.containsKey(user.getId())) {
            log.warn("user с id = {} не найден", user.getId());
            return Optional.empty();
        } else {
            if (user.getName() != null) {
                storage.get(user.getId()).setName(user.getName());
            }
            if (user.getEmail() != null) {
                storage.get(user.getId()).setEmail(user.getEmail());
            }
            log.info(storage.get(user.getId()) + " обновлен");
            return Optional.of(storage.get(user.getId()));
        }
    }

    @Override
    public void deleteUserById(long userId) {
        storage.remove(userId);
        log.info("user с id = {} удален", userId);
    }

    @Override
    public Optional<User> getUserById(long userId) {
        if (!storage.containsKey(userId)) {
            log.warn("user с id = {} не найден", userId);
            return Optional.empty();
        } else {
            log.info(storage.get(userId).toString() + " запрошен");
            return Optional.of(storage.get(userId));
        }
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(storage.values());
    }

    public boolean emailExistingCheck(String email) {
        return storage.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}

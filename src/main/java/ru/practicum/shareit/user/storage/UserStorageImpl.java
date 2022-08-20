package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.*;

import static ru.practicum.shareit.user.UserMapper.createUser;

/**
 * User storage into application memory
 */

@Slf4j
@Repository
public class UserStorageImpl implements UserStorage{
    private final Map<Long, User> storage = new HashMap<>();
    private long countId = 1;

    @Override
    public User addUser(UserDto userDto) {
        User user = createUser(countId++, userDto);
        log.info(user + " добавлен");
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> updateUser(long userId, UserDto userDto) {
        if (!storage.containsKey(userId)) {
            log.warn("user с id = {} не найден", userId);
            return Optional.empty();
        } else {
            if (userDto.getName() != null) {
                storage.get(userId).setName(userDto.getName());
            }
            if (userDto.getEmail() != null) {
                storage.get(userId).setEmail(userDto.getEmail());
            }
            log.info(storage.get(userId) + " обновлен");
            return Optional.of(storage.get(userId));
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

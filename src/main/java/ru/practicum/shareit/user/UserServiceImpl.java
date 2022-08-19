package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto addUser(UserDto user) throws EmailAlreadyExistException {
        if (userStorage.emailExistingCheck(user.getEmail())) {
            throw new EmailAlreadyExistException(String.format("Пользователь с email %s уже зарегистрирован",
                    user.getEmail()));
        }
        return UserMapper.convertToDto(userStorage.addUser(user));
    }

    @Override
    public UserDto updateUser(long userId, UserDto user) throws UserNotFoundException, EmailAlreadyExistException {
        if (userStorage.emailExistingCheck(user.getEmail())) {
            throw new EmailAlreadyExistException(String.format("Пользователь с email %s уже зарегистрирован",
                    user.getEmail()));
        }
        return userStorage.updateUser(userId, user).map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteUserById(long userId) throws UserNotFoundException {
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userStorage.deleteUserById(userId);
    }

    @Override
    public UserDto getUserById(long userId) throws UserNotFoundException {
        return userStorage.getUserById(userId).map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getAllUSer() {
        return userStorage.getAllUser().stream().map(UserMapper::convertToDto).collect(Collectors.toList());
    }
}

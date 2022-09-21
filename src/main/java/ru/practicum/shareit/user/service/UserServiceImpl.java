package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.toDto;
import static ru.practicum.shareit.user.UserMapper.toEntity;

@Slf4j
@Service
@RequiredArgsConstructor
@Qualifier("storage")
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private long countId = 1;

    @Override
    public UserDto addUser(UserDto userDto) {
        if (userStorage.emailExistingCheck(userDto.getEmail())) {
            throw new EmailAlreadyExistException(String.format("Пользователь с email %s уже зарегистрирован",
                    userDto.getEmail()));
        }
        userDto.setId(countId++);
        final User user = userStorage.addUser(toEntity(userDto));
        return toDto(user);
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        if (userStorage.emailExistingCheck(userDto.getEmail())) {
            throw new EmailAlreadyExistException(String.format("Пользователь с email %s уже зарегистрирован",
                    userDto.getEmail()));
        }
        userDto.setId(userId);
        return userStorage.updateUser(toEntity(userDto)).map(UserMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(userDto.getId()));
    }

    @Override
    public void deleteUserById(Long userId) {
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userStorage.deleteUserById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userStorage.getUserById(userId).map(UserMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getAllUSer() {
        return userStorage.getAllUser().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}

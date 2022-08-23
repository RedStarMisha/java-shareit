package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.user.UserMapper.convertToDto;
import static ru.practicum.shareit.user.UserMapper.toUser;

@Slf4j
@Service
@RequiredArgsConstructor
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
        final User user = userStorage.addUser(toUser(userDto));
        return convertToDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        if (userStorage.emailExistingCheck(userDto.getEmail())) {
            throw new EmailAlreadyExistException(String.format("Пользователь с email %s уже зарегистрирован",
                    userDto.getEmail()));
        }
        userDto.setId(userId);
        return userStorage.updateUser(toUser(userDto)).map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(userDto.getId()));
    }

    @Override
    public void deleteUserById(long userId) {
        userStorage.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userStorage.deleteUserById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        return userStorage.getUserById(userId).map(UserMapper::convertToDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getAllUSer() {
        return userStorage.getAllUser().stream().map(UserMapper::convertToDto).collect(Collectors.toList());
    }
}

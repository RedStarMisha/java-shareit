package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("repository")
public class UserServiceImpWithRepository implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpWithRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user;
        try {
            user = userRepository.save(UserMapper.toEntity(userDto));
        } catch (RuntimeException e) {
            throw new EmailAlreadyExistException(userDto.getEmail());
        }
        return UserMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserDto userDto) {
        return userRepository.findById(userId).map(user -> {
            User user1 = UserMapper.updateFromDto(user, userDto);
            userRepository.save(user1);
            return UserMapper.toDto(user1);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteUserById(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(long userId) {
        return userRepository.findById(userId).map(UserMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getAllUSer() {
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}

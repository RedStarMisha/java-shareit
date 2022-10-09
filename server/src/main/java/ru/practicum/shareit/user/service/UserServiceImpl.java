package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
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
    public UserDto updateUser(Long userId, UserDto userDto) {
        if (userDto.getEmail() != null && userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException(userDto.getEmail());
        }
        return userRepository.findById(userId).map(user -> {
            User user1 = UserMapper.updateFromDto(user, userDto);
            userRepository.save(user1);
            return UserMapper.toDto(user1);
        }).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.deleteById(userId);
    }

    @Override
    public UserDto getUserById(Long userId) {
        return userRepository.findById(userId).map(UserMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<UserDto> getAllUSer() {
        return userRepository.findAll().stream().map(UserMapper::toDto).collect(Collectors.toList());
    }
}

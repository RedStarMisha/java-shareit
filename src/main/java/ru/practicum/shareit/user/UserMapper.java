package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

public class UserMapper {

    public static UserDto convertToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User createUser(long id, UserDto userDto) {
        return new User(id, userDto.getName(), userDto.getEmail());
    }

}

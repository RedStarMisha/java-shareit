package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.UserDto;

import javax.validation.Valid;
import java.util.List;

/**
 * // User controller .
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto user) throws EmailAlreadyExistException {
        return userService.addUser(user);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto user)
            throws UserNotFoundException, EmailAlreadyExistException {
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) throws UserNotFoundException {
        userService.deleteUserById(userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUser() {
        return userService.getAllUSer();
    }
}

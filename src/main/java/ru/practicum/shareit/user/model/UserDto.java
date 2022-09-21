package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;
import ru.practicum.shareit.validation.IsText;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * UserDto class
 */
@Data
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = Create.class)
    @IsText(groups = Update.class)
    private String name;

    @NotNull(groups = Create.class)
    @Email(groups = {Create.class, Update.class})
    private String email;
}

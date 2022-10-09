package ru.practicum.shareit.controller.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import validation.Create;
import validation.IsText;
import validation.Update;

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

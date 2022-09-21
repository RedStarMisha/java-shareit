package ru.practicum.shareit.controllertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    @Qualifier("repository")
    UserService userService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldAddNewUser() throws Exception {
        UserDto userDto = makeUserDto(1L, "petya", "as@ya.ru");
        Mockito.when(userService.addUser(any()))
                .thenReturn(userDto);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(makeUserDto(null, "petya", "as@ya.ru")))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void should

    private static UserDto makeUserDto(Long id, String name, String email) {
        UserDto userDto = new UserDto(id, name, email);
        return userDto;
    }
}

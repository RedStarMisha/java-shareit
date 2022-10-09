package server.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exceptions.EmailAlreadyExistException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = {ShareItServer.class})
public class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    @Qualifier("repository")
    private UserService userService;

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
    void shouldUpdateNameAndEmail() throws Exception {
        UserDto userDto = makeUserDto(1L, "petya", "as@ya.ru");
        Mockito.when(userService.updateUser(anyLong(), any()))
                .thenReturn(userDto);

        mvc.perform(patch("/users/{userId}", 1L)
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
    void shouldReturn400WhenUpdateRepeatedEmail() throws Exception {
        Mockito.doThrow(new EmailAlreadyExistException("as@ya.ru")).when(userService).updateUser(anyLong(), any(UserDto.class));

        mvc.perform(patch("/users/{userId}", 1L)
                        .content(mapper.writeValueAsString(makeUserDto(null, "petya", "as@ya.ru")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(res -> assertEquals("email as@ya.ru уже зарегистрирован",
                        res.getResolvedException().getMessage()));
    }

    @Test
    void shouldReturn400WhenThrowUnknownExceptionWhenUpdate() throws Exception {
        Mockito.doThrow(new NullPointerException()).when(userService).updateUser(anyLong(), any(UserDto.class));

        mvc.perform(patch("/users/{userId}", 1L)
                        .content(mapper.writeValueAsString(makeUserDto(null, "petya", "as@ya.ru")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenDeleteNotExistingUser() throws Exception {
        Long userId = 1L;
        Mockito.doThrow(new UserNotFoundException(userId)).when(userService).deleteUserById(userId);
        mvc.perform(delete("/users/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(res -> assertEquals("Пользователь с id = 1 не найден",
                        res.getResolvedException().getMessage()));
    }

    @Test
    void shouldReturn200WhenGetUserById() throws Exception {
        Long userId = 1L;
        UserDto userDto = makeUserDto(userId, "petya", "as@ya.ru");
        Mockito.when(userService.getUserById(anyLong()))
                .thenReturn(userDto);

        mvc.perform(get("/users/{userId}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void shouldReturn404WhenGetNotExistingUser() throws Exception {
        Long userId = 1L;
        Mockito.doThrow(new UserNotFoundException(userId)).when(userService).getUserById(userId);
        mvc.perform(get("/users/{userId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(res -> assertEquals("Пользователь с id = 1 не найден", res.getResolvedException().getMessage()));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        UserDto userDto1 = makeUserDto(1L, "petya", "as@ya.ru");
        UserDto userDto2 = makeUserDto(2L, "rr", "sss@ya.ru");
        List<UserDto> serviceResponse = List.of(userDto1, userDto2);
        Mockito.when(userService.getAllUSer())
                .thenReturn(serviceResponse);

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto1.getName())))
                .andExpect(jsonPath("$[0].email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$[1].id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$[1].name", is(userDto2.getName())))
                .andExpect(jsonPath("$[1].email", is(userDto2.getEmail())));
    }

    @Test
    void shouldGetEmptyListWhenUsersNotFound() throws Exception {
        Mockito.when(userService.getAllUSer())
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private static UserDto makeUserDto(Long id, String name, String email) {
        UserDto userDto = new UserDto(id, name, email);
        return userDto;
    }
}

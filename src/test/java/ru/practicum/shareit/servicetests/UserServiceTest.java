package ru.practicum.shareit.servicetests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestUtil.makeUser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpWithRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
//@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private UserDto userDto;

    @BeforeEach
    private void create() {
        userService = new UserServiceImpWithRepository(userRepository);
        userDto = new UserDto(1L, "petya", "boy@ya.ru");
    }

    @Test
    void updateUserTest() {
        UserDto update = new UserDto(null, "dasha", "girl@ya.ru");
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(makeUser(1L, "petya", "boy@ya.ru")));

        UserDto updatedUser = userService.updateUser(1L, update);

        assertThat(updatedUser.getId(), is(1L));
        assertThat(updatedUser.getName(), is(update.getName()));
        assertThat(updatedUser.getEmail(), is(update.getEmail()));
    }


}

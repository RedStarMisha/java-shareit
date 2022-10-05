package controller.servicetests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import controller.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.model.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    private void create() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void updateUserTest() {
        UserDto update = new UserDto(null, "dasha", "girl@ya.ru");
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(TestUtil.makeUser(1L, "petya", "boy@ya.ru")));

        UserDto updatedUser = userService.updateUser(1L, update);

        assertThat(updatedUser.getId(), is(1L));
        assertThat(updatedUser.getName(), is(update.getName()));
        assertThat(updatedUser.getEmail(), is(update.getEmail()));
    }


}

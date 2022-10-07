package controller.servicetests;

import controller.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.requests.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.requests.service.RequestServiceImpl;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    private RequestService requestService;

    @BeforeEach
    void create() {
        requestService = new RequestServiceImpl(userRepository, requestRepository);
    }

    @Test
    void addFirstRequest() {
        Long userId = 1L;
        ItemRequestDtoEntry itemRequestDtoEntry = new ItemRequestDtoEntry("Запрос на банан");
        User user = TestUtil.makeUser(userId, "petya", "xx@ya.ru");

        Mockito.when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));
        Mockito.when(requestRepository.save(Mockito.any()))
                .thenReturn(TestUtil.makeItemRequest(1L, itemRequestDtoEntry.getDescription(), user, null));

        ItemRequestDto itemRequestDto = requestService.addRequest(userId, itemRequestDtoEntry);

        Mockito.verify(requestRepository, Mockito.times(1))
                .save(RequestMapper.toRequest(userRepository.findById(1L).get(), itemRequestDtoEntry));
        assertThat(itemRequestDto.getId(), is(1L));
        assertThat(itemRequestDto.getRequestor(), is(user.getId()));
        assertThat(itemRequestDto.getDescription(), is(itemRequestDtoEntry.getDescription()));
        assertThat(itemRequestDto.getItems(), empty());
        assertThat(itemRequestDto.getCreated(), notNullValue());
    }

    @Test
    void makePageable() {
        assertThat(ReflectionTestUtils.invokeMethod(requestService, "makePageParam", 0, 3),
                is(PageRequest.of(0, 3, Sort.by("created").descending())));
        assertThat(ReflectionTestUtils.invokeMethod(requestService, "makePageParam", 3, 3),
                is(PageRequest.of(1, 3, Sort.by("created").descending())));
    }

//    @Test
//    void makePageableWithIncorrectParameters() {
//        PaginationParametersException e1 = Assertions.assertThrows(
//                PaginationParametersException.class,
//                () -> ReflectionTestUtils.invokeMethod(requestService, "makePageParam", -1, 2));
//        PaginationParametersException e2 = Assertions.assertThrows(
//                PaginationParametersException.class,
//                () -> ReflectionTestUtils.invokeMethod(requestService, "makePageParam", 0, 0));
//        assertThat(e1.getMessage(), is("Неверные параметры страницы"));
//        assertThat(e2.getMessage(), is("Неверные параметры страницы"));
//    }


}

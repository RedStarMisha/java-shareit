package ru.practicum.shareit.servicetests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.exceptions.PaginationParametersException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.service.RequestServiceImpWithRepository;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RequestServiceImpWithRepository requestService;

    User user;

    @BeforeEach
    void create() {
        user = TestUtil.makeUser(1L, "petya", "xx@ya.ru");
    }

    @Test
    void addFirstRequest() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        ItemRequestDtoEntry itemRequestDtoEntry = new ItemRequestDtoEntry("Запрос на банан");
        Mockito.when(requestRepository.save(Mockito.any()))
                .thenReturn(TestUtil.makeItemRequest(1L, itemRequestDtoEntry.getDescription(), user, null));
        ItemRequestDto itemRequestDto = requestService.addRequest(1L, itemRequestDtoEntry);
        assertThat(itemRequestDto.getId(), is(1L));
        assertThat(itemRequestDto.getRequestor(), is(user.getId()));
        assertThat(itemRequestDto.getDescription(), is(itemRequestDtoEntry.getDescription()));
        assertThat(itemRequestDto.getItems(), empty());
        assertThat(itemRequestDto.getCreated(), notNullValue());
    }

    @Test
    void makePageable() {
        assertThat(ReflectionTestUtils.invokeMethod(requestService, "makePageParam", 0, 3)
                .equals(PageRequest.of(0, 3 , Sort.by("created").descending())), is(true));
        assertThat(ReflectionTestUtils.invokeMethod(requestService, "makePageParam", 3, 3)
                .equals(PageRequest.of(1, 3 , Sort.by("created").descending())), is(true));
    }

    @Test
    void makePageableWithIncorrectParameters() {
        PaginationParametersException e1 = Assertions.assertThrows(
                PaginationParametersException.class,
                () -> ReflectionTestUtils.invokeMethod(requestService, "makePageParam", -1, 2));
        PaginationParametersException e2 = Assertions.assertThrows(
                PaginationParametersException.class,
                () -> ReflectionTestUtils.invokeMethod(requestService, "makePageParam", 0, 0));
        assertThat(e1.getMessage(), is("Неверные параметры страницы"));
        assertThat(e2.getMessage(), is("Неверные параметры страницы"));
    }


}

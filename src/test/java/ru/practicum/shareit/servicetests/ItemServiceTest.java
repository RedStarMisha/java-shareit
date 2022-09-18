package ru.practicum.shareit.servicetests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.service.ItemServiceImpWithRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static ru.practicum.shareit.servicetests.TestUtil.makeItemRequest;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpWithRepository itemService;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestUtil.makeUser(1L, "petya", "xx@ya.ru");
    }

    void addFirstItemTest() {
        Mockito.when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Mockito.when(requestRepository.findById(1L))
                .thenReturn(Optional.of(makeItemRequest(1L, "Нужен банан", user, null)));

    }

    private ItemDtoShort makeItemDtoShort(Long id, String name, String description, Boolean available, Long requestId) {

    }
}

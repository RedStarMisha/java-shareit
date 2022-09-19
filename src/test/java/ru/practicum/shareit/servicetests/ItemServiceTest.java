package ru.practicum.shareit.servicetests;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.TestUtil;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.service.ItemServiceImpWithRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static ru.practicum.shareit.TestUtil.*;

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

    @Test
    void shouldUpdateItemWithoutRequest() {
        Long userId = 1L;
        Long itemId = 1L;

        Mockito.when(itemRepository.findByOwner_IdAndId(userId, itemId))
                .thenReturn(Optional.of(makeItem(itemId, "Банан", "Он желтый", user, true, null)));
        ItemDtoShort itemDtoShort = itemService.updateItem(userId, itemId, makeItemDtoShort(null, "Огурец",
                "Он зеленый", null, null));

        assertThat(itemDtoShort.getId(), is(itemId));
        assertThat(itemDtoShort.getAvailable(), is(true));
        assertThat(itemDtoShort.getRequestId(), nullValue());
        assertThat(itemDtoShort.getName(), is("Огурец"));
        assertThat(itemDtoShort.getDescription(), is("Он зеленый"));
    }

    private ItemDtoShort makeItemDtoShort(Long id, String name, String description, Boolean available, Long requestId) {
        ItemDtoShort itemDtoShort = new ItemDtoShort();
        itemDtoShort.setId(id);
        itemDtoShort.setName(name);
        itemDtoShort.setDescription(description);
        itemDtoShort.setAvailable(available);
        itemDtoShort.setRequestId(requestId);
        return itemDtoShort;
    }
}

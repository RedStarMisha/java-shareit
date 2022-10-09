package server.servicetests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import server.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    private ItemServiceImpl itemService;

    private User user;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, requestRepository,
                bookingRepository, commentRepository);
        user = TestUtil.makeUser(1L, "petya", "xx@ya.ru");
    }

    @Test
    void shouldUpdateItemWithoutRequest() {
        Long userId = 1L;
        Long itemId = 1L;

        Mockito.when(itemRepository.findByOwner_IdAndId(userId, itemId))
                .thenReturn(Optional.of(TestUtil.makeItem(itemId, "Банан", "Он желтый", user, true, null)));
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

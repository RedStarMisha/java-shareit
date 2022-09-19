package ru.practicum.shareit.repositorytest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestUtil.makeItemRequest;

import java.util.List;

@DataJpaTest
@Sql(scripts = "/schema.sql")
public class ItemRequestRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RequestRepository requestRepository;

    @BeforeEach
    @Sql(scripts = "/data.sql")
    void setUp() {

    }

    private User user;

    @Test
    void shouldGetAllRequestCreatedByOtherUsers() {
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        User user3 = userRepository.findById(3L).get();
        User user4 = userRepository.findById(4L).get();
        ItemRequest itemRequest1 = requestRepository.save(makeItemRequest(null, "банан", user1, null));
        ItemRequest itemRequest2 = requestRepository.save(makeItemRequest(null, "киви", user2, null));
        ItemRequest itemRequest3 = requestRepository.save(makeItemRequest(null, "яблоко", user3, null));
        ItemRequest itemRequest4 = requestRepository.save(makeItemRequest(null, "груша", user4, null));
        ItemRequest itemRequest5 = requestRepository.save(makeItemRequest(null, "слива", user4, null));
        List<ItemRequest> listForEqual = List.of(itemRequest3, itemRequest2, itemRequest1);
        Pageable pageable = PageRequest.of(0, 4 , Sort.by("created").descending());
        List<ItemRequest> list = requestRepository.findAllByOtherUser(user4.getId(), pageable);

        assertThat(list, hasSize(3));
        for (ItemRequest request : listForEqual) {
            assertThat(list, hasItem( allOf(
                    hasProperty("id", is(request.getId())),
                    hasProperty("description", is(request.getDescription())),
                    hasProperty("requestor", is(request.getRequestor()))
            )));
        }
    }


}

package ru.practicum.shareit.repositorytest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.TestUtil;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@DataJpaTest
@Sql(scripts = "/schema.sql")
public class ItemRequestTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User user;

    @BeforeEach
    @Sql("/data.sql")
    void setUp() {
        user = TestUtil.makeUser(null, "petya", "petya@ya.ru");
    }

    @Test
    void check() {

    }


}

package controller.repositorytest;

import controller.TestUtil;
import lombok.AllArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@ContextConfiguration(classes = {ShareItServer.class})
@AllArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "/create_four_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    void shouldSearchItemByNameAndDescription() {
        String text = "Ключ";
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        Item item1 = itemRepository.save(TestUtil.makeItem(null, "ключ для дрели", "дрель", user1,
                true, null));
        Item item2 = itemRepository.save(TestUtil.makeItem(null, "ключ на 10", "инструмент", user1,
                false, null));
        Item item3 = itemRepository.save(TestUtil.makeItem(null, "отвертка", "крестовая", user2,
                true, null));
        Item item4 = itemRepository.save(TestUtil.makeItem(null, "Домкрат", "вместе с КЛЮЧом", user2,
                true, null));
        Pageable pageable = PageRequest.of(0, 4, Sort.by("id").descending());

        List<Item> list = itemRepository.search(text, pageable);
        assertThat(list, hasSize(2));
        assertThat(list.get(0), Matchers.is(item4));
        assertThat(list.get(1), Matchers.is(item1));
    }
}

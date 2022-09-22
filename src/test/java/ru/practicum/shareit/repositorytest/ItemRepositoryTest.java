package ru.practicum.shareit.repositorytest;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestUtil.makeItem;

@DataJpaTest
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
        Item item1 = itemRepository.save(makeItem(null, "ключ для дрели", "дрель", user1,
                true, null));
        Item item2 = itemRepository.save(makeItem(null, "ключ на 10", "инструмент", user1,
                false, null));
        Item item3 = itemRepository.save(makeItem(null, "отвертка", "крестовая", user2,
                true, null));
        Item item4 = itemRepository.save(makeItem(null, "Домкрат", "вместе с КЛЮЧом", user2,
                true, null));
        Pageable pageable = PageRequest.of(0, 4, Sort.by("id").descending());

        List<Item> list = itemRepository.search(text, pageable);
        assertThat(list, hasSize(2));
        assertThat(list.get(0), is(item4));
        assertThat(list.get(1), is(item1));
    }
}

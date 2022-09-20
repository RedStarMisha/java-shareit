package ru.practicum.shareit.integrationtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static ru.practicum.shareit.TestUtil.makeItemDtoShort;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql(scripts = "/create_four_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class IntegrationItemServiceTest {

    private EntityManager entityManager;
    private ItemService itemService;

    @Autowired
    public IntegrationItemServiceTest(EntityManager entityManager, @Qualifier("repository") ItemService itemService) {
        this.entityManager = entityManager;
        this.itemService = itemService;
    }

    @Test
    void shouldAddItemWithoutRequest() {
        Long userId = 1L;
        ItemDtoShort itemDtoShort = makeItemDtoShort("банан", "он желтый", true, null);

        ItemDtoShort response = itemService.addItem(userId, itemDtoShort);
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i " +
                                    "where i.name=:name and i.description=:desc", Item.class);
        Item responseQuery = query.setParameter("name", itemDtoShort.getName())
                            .setParameter("desc", itemDtoShort.getDescription()).getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getName(), is(responseQuery.getName()));
        assertThat(response.getDescription(), is(response.getDescription()));
        assertThat(response.getAvailable(), is(responseQuery.getAvailable()));
    }
}

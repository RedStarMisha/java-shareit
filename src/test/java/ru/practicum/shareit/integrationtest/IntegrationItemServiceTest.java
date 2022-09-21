package ru.practicum.shareit.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.storage.RequestRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static ru.practicum.shareit.TestUtil.makeItemDtoShort;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql(scripts = {"/schema.sql", "/create_four_users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleandb.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationItemServiceTest {

    private EntityManager entityManager;
    private ItemService itemService;
    private RequestRepository requestRepository;

    @Autowired
    public IntegrationItemServiceTest(EntityManager entityManager, @Qualifier("repository") ItemService itemService,
                                      RequestRepository requestRepository) {
        this.entityManager = entityManager;
        this.itemService = itemService;
        this.requestRepository = requestRepository;
    }

    private ItemDtoShort itemDtoShort;

    @BeforeEach
    void setUp() {
        itemDtoShort = makeItemDtoShort("ИнструМент", "он желтый", true, null);
    }

    @Test
    void shouldAddItemWithoutRequest() {
        Long userId = 1L;

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

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_itemrequest.sql"})
    void shouldAddItemWithRequest() {
        Long userId = 1L;
        itemDtoShort = makeItemDtoShort("банан", "он желтый", true, 1L);

        ItemDtoShort response = itemService.addItem(userId, itemDtoShort);
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i " +
                "where i.name=:name and i.description=:desc", Item.class);
        Item responseQuery = query.setParameter("name", itemDtoShort.getName())
                .setParameter("desc", itemDtoShort.getDescription()).getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getName(), is(responseQuery.getName()));
        assertThat(response.getDescription(), is(responseQuery.getDescription()));
        assertThat(response.getAvailable(), is(responseQuery.getAvailable()));
        assertThat(response.getRequestId(), is(responseQuery.getRequest().getId()));
    }

    @Test
    void shouldUpdateItem() {
        Long userId = 1L;
        itemService.addItem(userId, itemDtoShort);
        ItemDtoShort update = makeItemDtoShort("груша", "зеленая", false, null);
        itemService.updateItem(userId, 1L, update);

        TypedQuery<Item> query = entityManager.createQuery("select i from Item i " +
                "where i.id=:id", Item.class);
        Item response = query.setParameter("id", 1L).getSingleResult();

        assertThat(response.getName(), is(update.getName()));
        assertThat(response.getDescription(), is(update.getDescription()));
        assertThat(response.getAvailable(), is(update.getAvailable()));
    }

    @Test
    void shouldGetItemById() {
        Long userId = 1L;
        itemService.addItem(userId, itemDtoShort);

        ItemDto response = itemService.getItemById(userId, 1L);
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i " +
                "where i.id=:id", Item.class);
        Item responseQuery = query.setParameter("id", 1L).getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getName(), is(responseQuery.getName()));
        assertThat(response.getDescription(), is(responseQuery.getDescription()));
        assertThat(response.getAvailable(), is(responseQuery.getAvailable()));
        assertThat(response.getRequest(), nullValue());
        assertThat(responseQuery.getRequest(), nullValue());
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql"})
    void shouldGetUserItems() {
        Long userId = 1L;

        List<ItemDto> response = itemService.getUserItems(userId, 0, 2);
        TypedQuery<Item> query = entityManager.createQuery("select i from Item i " +
                "where i.owner.id=:id", Item.class);
        List<Item> responseQuery = query.setParameter("id", userId).setFirstResult(0).setMaxResults(2).getResultList();

        assertThat(response, hasSize(responseQuery.size()));
        for (ItemDto itemDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(itemDto.getId())),
                    hasProperty("name", is(itemDto.getName())),
                    hasProperty("description", is(itemDto.getDescription())),
                    hasProperty("available", is(itemDto.getAvailable()))
            )));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql"})
    void shouldGetItemByName() {
        String text = "инструмент";

        List<ItemDtoShort> response = itemService.findItemByName(text, 0, 4);
        TypedQuery<Item> query = entityManager.createQuery(" select i from Item i " +
                "where (upper(i.name) like upper(concat('%', :text, '%')) " +
                " or upper(i.description) like upper(concat('%', :text, '%'))) " +
                " and i.available=true", Item.class);
        List<Item> responseQuery = query.setParameter("text", text).setFirstResult(0).setMaxResults(4).getResultList();

        assertThat(response, allOf(hasSize(3), hasSize(3)));
        for (ItemDtoShort itemDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(itemDto.getId())),
                    hasProperty("name", is(itemDto.getName())),
                    hasProperty("description", is(itemDto.getDescription())),
                    hasProperty("available", is(itemDto.getAvailable()))
            )));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldAddComment() {
        Long authorId = 1L;
        Long itemId = 1L;
        CommentDto commentDto = makeComment("Комментарий");

        CommentDto response = itemService.addComment(authorId, itemId, commentDto);
        TypedQuery<Comment> query = entityManager.createQuery("select c from Comment c " +
                "where c.id=:id", Comment.class);
        Comment responseQuery = query.setParameter("id", 1L).getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getText(), is(responseQuery.getText()));
        assertThat(response.getAuthorName(), is(responseQuery.getAuthor().getName()));
        assertThat(response.getItemId(), is(responseQuery.getItem().getId()));
        assertThat(response.getCreated(), is(responseQuery.getCreated()));
    }

    private CommentDto makeComment(String comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setText(comment);
        return commentDto;
    }
}

package ru.practicum.shareit.integrationtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.service.RequestService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@Sql(scripts = {"/schema.sql", "/create_four_users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleandb.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationRequestServiceTest {
    private EntityManager em;

    private RequestService requestService;


    @Autowired
    public IntegrationRequestServiceTest(EntityManager em, RequestService requestService) {
        this.em = em;
        this.requestService = requestService;
    }

    ItemRequestDtoEntry itemRequestDtoEntry1;
    ItemRequestDtoEntry itemRequestDtoEntry2;
    ItemRequestDtoEntry itemRequestDtoEntry3;

    @BeforeEach
    void setUp() {
        itemRequestDtoEntry1 = new ItemRequestDtoEntry("банан");
        itemRequestDtoEntry2 = new ItemRequestDtoEntry("апельсин");
        itemRequestDtoEntry3 = new ItemRequestDtoEntry("киви");
    }

    @Test
    void shouldSaveNewRequest() {
        Long userId = 1L;

        requestService.addRequest(userId, itemRequestDtoEntry1);

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r where r.description=:desc",
                ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("desc", itemRequestDtoEntry1.getDescription()).getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequest.getDescription(), is(itemRequestDtoEntry1.getDescription()));
        assertThat(itemRequest.getRequestor().getId(), is(userId));
        assertThat(itemRequest.getCreated(), notNullValue());
        assertThat(itemRequest.getItems(), nullValue());
    }

    @Test
    void shouldGetRequestById() {
        Long userId = 1L;
        Long requestId = 1L;
        requestService.addRequest(userId, itemRequestDtoEntry1);

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r where r.id=:id",
                ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", requestId).getSingleResult();
        ItemRequestDto itemRequestDto = requestService.getRequest(userId, requestId);

        assertThat(itemRequest.getId(), is(itemRequestDto.getId()));
        assertThat(itemRequest.getRequestor().getId(), is(itemRequestDto.getRequestor()));
        assertThat(itemRequest.getDescription(), is(itemRequestDto.getDescription()));
        assertThat(itemRequest.getCreated(), is(itemRequestDto.getCreated()));
    }

    @Test
    void shouldGetUserRequests() {
        Long userId = 1L;
        requestService.addRequest(userId, itemRequestDtoEntry1);
        requestService.addRequest(userId, itemRequestDtoEntry2);
        requestService.addRequest(2L, itemRequestDtoEntry3);

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r " +
                "where r.requestor.id=:id order by r.created desc", ItemRequest.class);
        List<ItemRequest> itemRequests = query.setParameter("id", userId).getResultList();
        List<ItemRequestDto> requestDtos = requestService.getUserRequests(userId);

        assertThat(itemRequests, hasSize(requestDtos.size()));
        for (ItemRequest itemRequest : itemRequests) {
            assertThat(requestDtos, hasItem(allOf(
                    hasProperty("id", is(itemRequest.getId())),
                    hasProperty("description", is(itemRequest.getDescription())),
                    hasProperty("requestor", is(itemRequest.getRequestor().getId())),
                    hasProperty("created", is(itemRequest.getCreated()))
            )));
        }
    }

    @Test
    void shouldGetRequestWhatCanBeAnswered() {
        Long userId = 1L;
        requestService.addRequest(userId, itemRequestDtoEntry1);
        requestService.addRequest(2L, itemRequestDtoEntry2);
        requestService.addRequest(3L, itemRequestDtoEntry3);

        TypedQuery<ItemRequest> query = em.createQuery("select r from ItemRequest r " +
                "where r.requestor.id<>:id order by r.created desc", ItemRequest.class);
        List<ItemRequest> itemRequests = query.setParameter("id", userId).getResultList();
        List<ItemRequestDto> requestDtos = requestService.getRequests(userId, 0, 2);

        assertThat(itemRequests, hasSize(requestDtos.size()));
        for (ItemRequest itemRequest : itemRequests) {
            assertThat(requestDtos, hasItem(allOf(
                    hasProperty("id", is(itemRequest.getId())),
                    hasProperty("description", is(itemRequest.getDescription())),
                    hasProperty("requestor", is(itemRequest.getRequestor().getId())),
                    hasProperty("created", is(itemRequest.getCreated()))
            )));
        }
    }
}

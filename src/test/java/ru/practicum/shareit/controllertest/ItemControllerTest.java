package ru.practicum.shareit.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.exceptions.CommentCreationException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtil.makeItemDto;
import static ru.practicum.shareit.TestUtil.makeItemDtoShort;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    @Qualifier("repository")
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    void addItem() throws Exception {
        Long ownerId = 1L;
        ItemDtoShort request = makeItemDtoShort(null, "банан", "он желтый", true, null);
        ItemDtoShort response = makeItemDtoShort(1L, "банан", "он желтый", true, null);

        Mockito.when(itemService.addItem(anyLong(), ArgumentMatchers.any(ItemDtoShort.class)))
                .thenReturn(response);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.available", is(response.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", nullValue(), Long.class));
    }

    @Test
    void shouldReturn404WhenRequestNotFound() throws Exception {
        Long ownerId = 1L;
        ItemDtoShort request = makeItemDtoShort(null, "банан", "он желтый", true, 1L);

        Mockito.doThrow(new RequestNotFoundException(1L)).when(itemService).addItem(ownerId, request);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(res -> assertEquals("Запрос с id = 1 не найден",
                        res.getResolvedException().getMessage()));
    }

    @Test
    void updateItem() throws Exception {
        Long ownerId = 1L;
        Long itemId = 1L;
        ItemDtoShort request = makeItemDtoShort(null, "банан", "он желтый", true, null);
        ItemDtoShort response = makeItemDtoShort(1L, "банан", "он желтый", true, null);

        Mockito.when(itemService.updateItem(anyLong(), anyLong(), ArgumentMatchers.any(ItemDtoShort.class)))
                .thenReturn(response);
        mvc.perform(patch("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", ownerId)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.available", is(response.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", nullValue(), Long.class));
    }

    @Test
    void getItem() throws Exception {
        Long ownerId = 1L;
        Long itemId = 1L;
        ItemDto response = makeItemDto(1L, "банан", "желтый", true, null,
                new BookingShort(), new BookingShort(), Collections.emptyList());

        Mockito.when(itemService.getItemById(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", ownerId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(response.getName())))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.available", is(response.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.request", nullValue(), Long.class))
                .andExpect(jsonPath("$.nextBooking", notNullValue()))
                .andExpect(jsonPath("$.lastBooking", notNullValue()))
                .andExpect(jsonPath("$.comments", empty()));
    }

    @Test
    void should404WhenItemNotFound() throws Exception {
        Long ownerId = 1L;
        Long itemId = 1L;

        Mockito.doThrow(new ItemNotFoundException(1L)).when(itemService)
                .getItemById(anyLong(), anyLong());

        mvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", ownerId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(res -> assertEquals("Вещь с id = 1 не найдена",
                        res.getResolvedException().getMessage()));
    }

    @Test
    void shouldReturn400WhenCommentException() throws Exception {
        Long userId = 1L;
        Long itemId = 1L;

        Mockito.doThrow(new CommentCreationException(String.format("User с id = %d" +
                        " не может оставить комментарий Item c id = %d", userId, itemId))).
                when(itemService).addComment(anyLong(), anyLong(), ArgumentMatchers.any(CommentDto.class));

        CommentDto request = makeCommentDto(null, "текст", null, null, null);
        ;

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", userId)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(res -> assertEquals("User с id = 1 не может оставить комментарий Item c id = 1",
                        res.getResolvedException().getMessage()));
    }

    @Test
    void getOwnerItems() throws Exception {
        Long ownerId = 1L;
        ItemDto item1 = makeItemDto(1L, "банан", "желтый", true, null,
                new BookingShort(), new BookingShort(), Collections.emptyList());
        ItemDto item2 = makeItemDto(2L, "желудь", "красный", true, null,
                new BookingShort(), new BookingShort(), Collections.emptyList());
        List<ItemDto> response = List.of(item1, item2);

        Mockito.when(itemService.getUserItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(response);

        ResultActions actions = mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(response.size())));
        for (int i = 0; i < response.size(); i++) {
            actions
                    .andExpect(jsonPath("$[" + i + "].id", is(response.get(i).getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].name", is(response.get(i).getName())))
                    .andExpect(jsonPath("$[" + i + "].description", is(response.get(i).getDescription())))
                    .andExpect(jsonPath("$[" + i + "].available", is(response.get(i).getAvailable()), Boolean.class))
                    .andExpect(jsonPath("$[" + i + "].request", nullValue(), Long.class))
                    .andExpect(jsonPath("$[" + i + "].nextBooking", notNullValue()))
                    .andExpect(jsonPath("$[" + i + "].lastBooking", notNullValue()))
                    .andExpect(jsonPath("$[" + i + "].comments", empty()));
        }
    }

    @Test
    void findItemsByName() throws Exception {
        Long ownerId = 1L;
        ItemDtoShort item1 = makeItemDtoShort(1L, "банан", "желтый", true, null);
        ItemDtoShort item2 = makeItemDtoShort(2L, "желудь", ",банановый мусс", true, null);
        List<ItemDtoShort> response = List.of(item1, item2);

        Mockito.when(itemService.findItemByName(anyString(), anyInt(), anyInt()))
                .thenReturn(response);

        ResultActions actions = mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("text", "банан")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(response.size())));
        for (int i = 0; i < response.size(); i++) {
            actions
                    .andExpect(jsonPath("$[" + i + "].id", is(response.get(i).getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].name", is(response.get(i).getName())))
                    .andExpect(jsonPath("$[" + i + "].description", is(response.get(i).getDescription())))
                    .andExpect(jsonPath("$[" + i + "].available", is(response.get(i).getAvailable()), Boolean.class))
                    .andExpect(jsonPath("$[" + i + "].requestId", nullValue(), Long.class));
        }
    }

    @Test
    void addComment() throws Exception {
        Long authorId = 1L;
        Long itemId = 1L;
        CommentDto request = makeCommentDto(null, "текст", null, null, null);
        CommentDto response = makeCommentDto(1L, "текст", "имя", itemId, LocalDateTime.now());

        Mockito.when(itemService.addComment(anyLong(), anyLong(), ArgumentMatchers.any(CommentDto.class)))
                .thenReturn(response);

        mvc.perform(post("/items/{itemId}/comment", itemId)
                        .header("X-Sharer-User-Id", authorId)
                        .content(mapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.text", allOf(is(response.getText()), is(request.getText()))))
                .andExpect(jsonPath("$.authorName", is(response.getAuthorName())))
                .andExpect(jsonPath("$.itemId", is(response.getItemId()), Long.class))
                .andExpect(jsonPath("$.created", notNullValue(), LocalDateTime.class));
    }

    private CommentDto makeCommentDto(Long id, String text, String authorName, Long itemId, LocalDateTime created) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setText(text);
        commentDto.setAuthorName(authorName);
        commentDto.setItemId(itemId);
        commentDto.setCreated(created);
        return commentDto;
    }
}
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
import ru.practicum.shareit.TestUtil;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    @Qualifier("repository")
    private RequestService requestService;

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldAddRequest() throws Exception {
        Long requestorId = 1L;
        ItemRequestDtoEntry itemRequestDtoEntry = new ItemRequestDtoEntry("нужен банан");
        ItemRequestDto response = TestUtil.makeItemRequestDto(1L, itemRequestDtoEntry.getDescription(), requestorId,
                LocalDateTime.now(), null);

        Mockito.when(requestService.addRequest(anyLong(), any()))
                .thenReturn(response);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", requestorId)
                        .content(mapper.writeValueAsString(itemRequestDtoEntry))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.requestor", is(requestorId), Long.class))
                .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    void shouldGetRequestByIdAndRequestorId() throws Exception {
        Long requestorId = 1L;
        Long requestId = 1L;
        ItemRequestDto response = TestUtil.makeItemRequestDto(1L, "банан", requestorId,
                LocalDateTime.now(), null);

        Mockito.when(requestService.getRequest(requestorId, requestId))
                .thenReturn(response);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", requestorId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(response.getDescription())))
                .andExpect(jsonPath("$.requestor", is(requestorId), Long.class))
                .andExpect(jsonPath("$.created", notNullValue()));
    }

    @Test
    void getUserRequests() throws Exception {
        Long requestorId = 1L;
        ItemRequestDto request1 = TestUtil.makeItemRequestDto(1L, "банан", requestorId,
                LocalDateTime.now(), null);
        ItemRequestDto request2 = TestUtil.makeItemRequestDto(2L, "киви", requestorId,
                LocalDateTime.now(), null);
        ItemRequestDto request3 = TestUtil.makeItemRequestDto(3L, "желудь", requestorId,
                LocalDateTime.now(), null);
        List<ItemRequestDto> list = List.of(request1, request2, request3);

        Mockito.when(requestService.getUserRequests(requestorId))
                .thenReturn(list);

        ResultActions actions = mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", requestorId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        for (int i = 0; i < list.size(); i++) {
            actions
                    .andExpect(jsonPath("$[" + i + "].id", is(list.get(i).getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].description", is(list.get(i).getDescription())))
                    .andExpect(jsonPath("$[" + i + "].requestor", is(requestorId), Long.class))
                    .andExpect(jsonPath("$[" + i + "].created", notNullValue()));
        }
    }

    @Test
    void getRequests() throws Exception {
        Long userId = 1L;
        ItemRequestDto request1 = TestUtil.makeItemRequestDto(1L, "банан", 2L,
                LocalDateTime.now(), null);
        ItemRequestDto request2 = TestUtil.makeItemRequestDto(2L, "киви", 2L,
                LocalDateTime.now(), null);
        ItemRequestDto request3 = TestUtil.makeItemRequestDto(3L, "желудь", 2L,
                LocalDateTime.now(), null);
        List<ItemRequestDto> list = List.of(request1, request2, request3);

        Mockito.when(requestService.getRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(list);
        ResultActions actions = mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", "0")
                        .param("size", "3")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        for (int i = 0; i < list.size(); i++) {
            actions
                    .andExpect(jsonPath("$[" + i + "].id", is(list.get(i).getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].description", is(list.get(i).getDescription())))
                    .andExpect(jsonPath("$[" + i + "].requestor", not(userId), Long.class))
                    .andExpect(jsonPath("$[" + i + "].created", notNullValue()));
        }
    }
}

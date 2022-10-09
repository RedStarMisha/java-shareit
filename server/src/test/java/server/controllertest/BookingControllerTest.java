package server.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import server.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.notfound.BookingNotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = {ShareItServer.class})
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    private LocalDateTime start;
    private LocalDateTime end;

    private ItemDtoShort itemDtoShort;

    private User booker;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusDays(1);
        end = LocalDateTime.now().plusDays(2);
        itemDtoShort = TestUtil.makeItemDtoShort(1L, "asd", "gs", true, null);
        booker = TestUtil.makeUser(1L, "vvv", "das@ya.ru");
    }

    @Test
    void addBooking() throws Exception {
        BookingDtoEntry request = TestUtil.makeBookingDtoEntry(start, end, itemDtoShort.getId());
        BookingDto response = TestUtil.makeBookingDto(1L, start, end, itemDtoShort, booker, BookingStatus.WAITING);

        Mockito.when(bookingService.addBooking(anyLong(), ArgumentMatchers.any(BookingDtoEntry.class)))
                .thenReturn(response);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(request))
                        .header("X-Sharer-User-Id", booker.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(request.getItemId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(booker.getId()), Long.class))
                .andExpect(jsonPath("$.status", is("WAITING")));
    }

    @Test
    void approveStatus() throws Exception {
        BookingDto response = TestUtil.makeBookingDto(1L, start, end, itemDtoShort, booker, BookingStatus.APPROVED);

        Mockito.when(bookingService.approveStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(response);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(1L), Long.class))
                .andExpect(jsonPath("$.booker.id", is(1L), Long.class))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void getBooking() throws Exception {
        BookingDto response = TestUtil.makeBookingDto(1L, start, end, itemDtoShort, booker, BookingStatus.APPROVED);

        Mockito.when(bookingService.getBookingByUserIdAndBookingId(anyLong(), anyLong()))
                .thenReturn(response);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", booker.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(response.getId()), Long.class))
                .andExpect(jsonPath("$.start", notNullValue()))
                .andExpect(jsonPath("$.end", notNullValue()))
                .andExpect(jsonPath("$.item.id", is(1L), Long.class))
                .andExpect(jsonPath("$.booker.id", is(1L), Long.class))
                .andExpect(jsonPath("$.status", is("APPROVED")));
    }

    @Test
    void shouldReturn404WhenBookingBookingNotFound() throws Exception {
        Mockito.doThrow(new BookingNotFoundException(100L)).when(bookingService)
                .getBookingByUserIdAndBookingId(anyLong(), anyLong());

        mvc.perform(get("/bookings/{bookingId}", 100L)
                        .header("X-Sharer-User-Id", booker.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(res -> assertEquals("Бронирование с id = 100 не найдено",
                        res.getResolvedException().getMessage()));
    }

    @Test
    void getUserBooking() throws Exception {
        BookingDto booking1 = TestUtil.makeBookingDto(1L, start, end, itemDtoShort, booker, BookingStatus.APPROVED);
        BookingDto booking2 = TestUtil.makeBookingDto(2L, start, end, itemDtoShort, booker, BookingStatus.WAITING);
        List<BookingDto> response = List.of(booking1, booking2);

        Mockito.when(bookingService.getBookingByStateAndBookerId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(response);

        ResultActions actions = mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", booker.getId())
                        .param("state", "ALL")
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
                    .andExpect(jsonPath("$[" + i + "].start", notNullValue()))
                    .andExpect(jsonPath("$[" + i + "].end", notNullValue()))
                    .andExpect(jsonPath("$[" + i + "].item.id", is(response.get(i).getItem().getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].booker.id", is(response.get(i).getBooker().getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].status", is(response.get(i).getStatus().toString())));
        }
    }

    @Test
    void getBookingForUsersItem() throws Exception {
        BookingDto booking1 = TestUtil.makeBookingDto(1L, start, end, itemDtoShort, booker, BookingStatus.APPROVED);
        BookingDto booking2 = TestUtil.makeBookingDto(2L, start, end, itemDtoShort, booker, BookingStatus.WAITING);
        List<BookingDto> response = List.of(booking1, booking2);

        Mockito.when(bookingService.getBookingByStateAndItemsOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(response);

        ResultActions actions = mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2L)
                        .param("state", "ALL")
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
                    .andExpect(jsonPath("$[" + i + "].start", notNullValue()))
                    .andExpect(jsonPath("$[" + i + "].end", notNullValue()))
                    .andExpect(jsonPath("$[" + i + "].item.id", is(response.get(i).getItem().getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].booker.id", is(response.get(i).getBooker().getId()), Long.class))
                    .andExpect(jsonPath("$[" + i + "].status", is(response.get(i).getStatus().toString())));
        }
    }
}
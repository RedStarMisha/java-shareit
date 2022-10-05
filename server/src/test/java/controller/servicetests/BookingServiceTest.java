package controller.servicetests;

import controller.TestUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;
import ru.practicum.shareit.booking.strategy.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.strategy.StrategyFactory;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.booking.BookingMapper.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private StrategyFactory strategyFactory;

    private BookingServiceImpl bookingService;

    private User booker;
    private User itemOwner;
    private Item item;

    Long itemId = 1L;
    Long bookerId = 1L;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository, strategyFactory);
        itemOwner = TestUtil.makeUser(2L, "kolya", "xx1@ya.ru");
        item = TestUtil.makeItem(1L, "банан", "желтый", itemOwner, true, null);
        booker = TestUtil.makeUser(bookerId, "petya", "xx@ya.ru");
    }

    @Test
    void shouldAddBooking() {
        BookingDtoEntry bookingDtoEntry = makeBookingEntry(LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3), itemId);
        Mockito.when(userRepository.findById(bookerId))
                .thenReturn(Optional.of(booker));
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));
        Booking booking = toBooking(booker, item, bookingDtoEntry);

        Mockito.when(bookingRepository.save(Mockito.any(Booking.class)))
                .thenReturn(setBookingId(1L, booking));
        BookingDto bookingDto = bookingService.addBooking(bookerId, bookingDtoEntry);

        assertThat(bookingDto.getId(), is(1L));
        assertThat(bookingDto.getStart(), is(bookingDtoEntry.getStart()));
        assertThat(booking.getEnd(), is(bookingDtoEntry.getEnd()));
        assertThat(booking.getBooker(), Matchers.is(booker));
        assertThat(booking.getStatus(), Matchers.is(BookingStatus.WAITING));
        assertThat(booking.getItem(), Matchers.is(item));
    }

    @Test
    void shouldThrowExceptionWhenBookingAddByOwner() {
        Long bookerId = 2L;

        booker = TestUtil.makeUser(bookerId, "petya", "xx@ya.ru");
        BookingDtoEntry bookingDtoEntry = makeBookingEntry(LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3), itemId);
        Mockito.when(userRepository.findById(bookerId))
                .thenReturn(Optional.of(booker));
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));

        BookingCreationException e = Assertions.assertThrows(BookingCreationException.class,
                () -> bookingService.addBooking(bookerId, bookingDtoEntry));
        assertThat(e.getMessage(), is("Пользователь не может арендовать вещь сам у себя"));
    }

    @Test
    void shouldThrowExceptionWhenItemIsNotAvailable() {
        item.setAvailable(false);

        BookingDtoEntry bookingDtoEntry = makeBookingEntry(LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3), itemId);
        Mockito.when(userRepository.findById(bookerId))
                .thenReturn(Optional.of(booker));
        Mockito.when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(item));


        ItemAvailableException e = Assertions.assertThrows(ItemAvailableException.class,
                () -> bookingService.addBooking(bookerId, bookingDtoEntry));
        assertThat(e.getMessage(), is("item с id = " + itemId + " не доступна"));
    }

    @Test
    void shouldApproveBookingStatus() {
        Long bookingId = 1L;
        Booking booking = TestUtil.makeBooking(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                item, booker);

        Mockito.when(bookingRepository.findByIdAndItem_Owner_Id(bookingId, item.getOwner().getId()))
                .thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.approveStatus(item.getOwner().getId(), bookingId, true);

        assertThat(bookingDto.getId(), is(1L));
        assertThat(bookingDto.getStart(), is(booking.getStart()));
        assertThat(booking.getEnd(), is(booking.getEnd()));
        assertThat(booking.getBooker(), Matchers.is(booker));
        assertThat(booking.getStatus(), Matchers.is(BookingStatus.APPROVED));
        assertThat(booking.getItem(), Matchers.is(item));
    }

    @Test
    void shouldNotApproveStatusBookingStatus() {
        Long bookingId = 1L;
        Booking booking = TestUtil.makeBooking(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                item, booker);

        Mockito.when(bookingRepository.findByIdAndItem_Owner_Id(bookingId, item.getOwner().getId()))
                .thenReturn(Optional.of(booking));

        BookingDto bookingDto = bookingService.approveStatus(item.getOwner().getId(), bookingId, false);

        assertThat(bookingDto.getId(), is(1L));
        assertThat(bookingDto.getStart(), is(booking.getStart()));
        assertThat(booking.getEnd(), is(booking.getEnd()));
        assertThat(booking.getBooker(), Matchers.is(booker));
        assertThat(booking.getStatus(), Matchers.is(BookingStatus.REJECTED));
        assertThat(booking.getItem(), Matchers.is(item));
    }

    @Test
    void shouldThrowExceptionWhenBaseBookingStatusIsNotWaiting() {
        Long bookingId = 1L;
        Booking booking = TestUtil.makeBooking(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                item, booker);
        booking.setStatus(BookingStatus.REJECTED);

        Mockito.when(bookingRepository.findByIdAndItem_Owner_Id(bookingId, item.getOwner().getId()))
                .thenReturn(Optional.of(booking));

        BookingStatusException e = Assertions.assertThrows(BookingStatusException.class,
                () -> bookingService.approveStatus(item.getOwner().getId(), bookingId, true));
        assertThat(e.getMessage(), is("У аренды нельзя изменить статус"));
    }

    @Test
    void shouldGetBookingStateWaiting() {
        BookingState state = ReflectionTestUtils.invokeMethod(bookingService, "getBookingState",
                "WAITING");

        assertThat(state, Matchers.is(BookingState.WAITING));
    }

    @Test
    void shouldThrowExceptionWhenUnknownState() {
        UnknownBookingStateException e = Assertions.assertThrows(UnknownBookingStateException.class,
                () -> ReflectionTestUtils.invokeMethod(bookingService, "getBookingState",
                        "SUPER"));

        assertThat(e.getMessage(), is("Unknown state: SUPER"));
    }

    @Test
    void makePageable() {
        assertThat(ReflectionTestUtils.invokeMethod(bookingService, "makePageParam", 0, 3),
                is(PageRequest.of(0, 3, Sort.by("start").descending())));
        assertThat(ReflectionTestUtils.invokeMethod(bookingService, "makePageParam", 3, 3),
                is(PageRequest.of(1, 3, Sort.by("start").descending())));
    }

    @Test
    void makePageableWithIncorrectParameters() {
        PaginationParametersException e1 = Assertions.assertThrows(
                PaginationParametersException.class,
                () -> ReflectionTestUtils.invokeMethod(bookingService, "makePageParam", -1, 2));
        PaginationParametersException e2 = Assertions.assertThrows(
                PaginationParametersException.class,
                () -> ReflectionTestUtils.invokeMethod(bookingService, "makePageParam", 0, 0));
        assertThat(e1.getMessage(), is("Неверные параметры страницы"));
        assertThat(e2.getMessage(), is("Неверные параметры страницы"));
    }


    private BookingDtoEntry makeBookingEntry(LocalDateTime start, LocalDateTime end, Long itemId) {
        return new BookingDtoEntry(start, end, itemId);
    }

    private Booking setBookingId(long bookingId, Booking booking) {
        booking.setId(bookingId);
        return booking;
    }

    private List<BookingDto> makeListOfBooking() {
        Booking bookingF1 = TestUtil.makeBooking(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                item, booker);
        bookingF1.setStatus(BookingStatus.REJECTED);
        Booking bookingP1 = TestUtil.makeBooking(2L, LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1),
                item, booker);
        Booking bookingP2 = TestUtil.makeBooking(3L, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2),
                item, booker);
        Booking bookingF2 = TestUtil.makeBooking(4L, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4),
                item, booker);
        Booking bookingC = TestUtil.makeBooking(5L, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(3),
                item, booker);
        List<BookingDto> list = new ArrayList<>();
        list.add(toBookingDto(bookingP2));
        list.add(toBookingDto(bookingP1));
        list.add(toBookingDto(bookingC));
        list.add(toBookingDto(bookingF1));
        list.add(toBookingDto(bookingF2));
        return list;
    }

}

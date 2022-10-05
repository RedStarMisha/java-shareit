package controller.integrationtest;

import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/cleandb.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class IntegrationBookingServiceTest {

    private final EntityManager em;
    private final BookingService bookingService;

    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        start = LocalDateTime.now().plusDays(2);
        end = LocalDateTime.now().plusDays(3);
    }

    @Test
    void shouldAddBooking() {
        Long itemId = 1L;
        Long bookerId = 1L;
        BookingDtoEntry bookingDtoEntry = new BookingDtoEntry(start, end, itemId);

        BookingDto response = bookingService.addBooking(bookerId, bookingDtoEntry);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.item.id=:itemId and b.booker.id=:bookerId", Booking.class);
        Booking responseQuery = query.setParameter("itemId", itemId).setParameter("bookerId", bookerId)
                .getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getStart(), is(responseQuery.getStart()));
        assertThat(response.getStatus(), allOf(is(responseQuery.getStatus()), Matchers.is(BookingStatus.WAITING)));
        assertThat(response.getItem().getId(), is(responseQuery.getItem().getId()));
        assertThat(response.getBooker(), is(responseQuery.getBooker()));
    }

    @Test
    void shouldApproveStatus() {
        Long ownerItemId = 2L;
        BookingDtoEntry bookingDtoEntry = new BookingDtoEntry(start, end, 1L);
        bookingService.addBooking(1L, bookingDtoEntry);

        BookingDto response = bookingService.approveStatus(ownerItemId, 1L, true);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.id=:bookingId", Booking.class);
        Booking responseQuery = query.setParameter("bookingId", 1L).getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getStatus(), allOf(is(responseQuery.getStatus()), Matchers.is(BookingStatus.APPROVED)));
    }

    @Test
    void shouldGetBookingByBookerOrOwnerIdIdAndBookingId() {
        Long ownerItemId = 2L;
        Long bookerId = 1L;
        BookingDtoEntry bookingDtoEntry = new BookingDtoEntry(start, end, 1L);
        bookingService.addBooking(bookerId, bookingDtoEntry);

        BookingDto response = bookingService.getBookingByUserIdAndBookingId(ownerItemId, 1L);
        BookingDto responseByBooker = bookingService.getBookingByUserIdAndBookingId(bookerId, 1L);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.id=:bookingId and b.booker.id=:bookerId", Booking.class);
        Booking responseQuery = query.setParameter("bookingId", 1L).setParameter("bookerId", bookerId)
                .getSingleResult();

        assertThat(response.getId(), is(responseQuery.getId()));
        assertThat(response.getStart().toLocalDate(), is(responseQuery.getStart().toLocalDate()));
        assertThat(response.getStatus(), allOf(is(responseQuery.getStatus()), Matchers.is(BookingStatus.WAITING)));
        assertThat(response.getItem().getId(), is(responseQuery.getItem().getId()));
        assertThat(response, Matchers.is(responseByBooker));
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetUserBookingByStateFuture() {
        Long bookerId = 3L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "future", 0, 2);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.booker.id=:bookerId and b.start > :date order by b.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(2).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(2))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetUserBookingByStateCurrent() {
        Long bookerId = 3L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "current", 0, 2);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.booker.id=:bookerId and :date between b.start and b.end order by b.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(2).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(1))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetUserBookingByStateAll() {
        Long bookerId = 3L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "all", 0, 4);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.booker.id=:bookerId", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setFirstResult(0)
                .setMaxResults(4).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(3))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetUserBookingByStatePast() {
        Long bookerId = 1L;

        List<BookingDto> response = bookingService.getUserBookingByState(bookerId, "past", 0, 3);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.booker.id=:bookerId and b.end < :date order by b.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("bookerId", bookerId).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(3).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(1))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
            assertThat(bookingDto.getBooker().getId(), is(bookerId));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetBookingByItemOwnerAndStateRejected() {
        Long itemsOwner = 1L;

        List<BookingDto> response = bookingService.getBookingForUsersItem(itemsOwner, "rejectEd", 0, 2);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.item.owner.id=:ownerId and b.status = :status order by b.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("ownerId", itemsOwner).setParameter("status", BookingStatus.REJECTED)
                .setFirstResult(0).setMaxResults(4).getResultList();
        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(2))));

        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetBookingByItemOwnerAndStateFuture() {
        Long itemsOwner = 1L;

        List<BookingDto> response = bookingService.getBookingForUsersItem(itemsOwner, "Future", 0, 2);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.item.owner.id=:ownerId and b.start > :date order by b.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("ownerId", itemsOwner).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(4).getResultList();
        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(2))));

        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
        }
    }

    @Test
    @Sql(scripts = {"/schema.sql", "/create_four_users.sql", "/create_four_item.sql", "/create_booking.sql"})
    void shouldGetBookingByItemOwnerAndStatePast() {
        Long itemsOwner = 1L;

        List<BookingDto> response = bookingService.getBookingForUsersItem(itemsOwner, "Past", 0, 2);
        TypedQuery<Booking> query = em.createQuery("select b from Booking b " +
                "where b.item.owner.id=:ownerId and b.end < :date order by b.start desc", Booking.class);
        List<Booking> responseQuery = query.setParameter("ownerId", itemsOwner).setParameter("date", LocalDateTime.now())
                .setFirstResult(0).setMaxResults(4).getResultList();

        assertThat(response, hasSize(allOf(is(responseQuery.size()), is(1))));
        for (BookingDto bookingDto : response) {
            assertThat(responseQuery, hasItem(allOf(
                    hasProperty("id", is(bookingDto.getId())),
                    hasProperty("booker", is(bookingDto.getBooker())),
                    hasProperty("item", hasProperty("id", is(bookingDto.getItem().getId()))),
                    hasProperty("start", is(bookingDto.getStart())),
                    hasProperty("end", is(bookingDto.getEnd()))
            )));
        }
    }
}

package ru.practicum.shareit.repositorytest;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static ru.practicum.shareit.TestUtil.makeBooking;
import static ru.practicum.shareit.TestUtil.makeItem;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AllArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "/create_four_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BookingRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Test
    void shouldGetBookingByIdAndOwnerAndBookerId() {
        User user1 = userRepository.findById(1L).get();
        User user2 = userRepository.findById(2L).get();
        User user3 = userRepository.findById(3L).get();
        Item item = itemRepository.save(makeItem(null, "банан", "желтый", user1, true,
                null));
        Booking booking1 = makeBooking(null, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3),
                item, user2);
        booking1 = bookingRepository.save(booking1);

        Booking receivedBooking = bookingRepository.findBooking(1L, 1L).get();
        assertThat(receivedBooking.getId(), is(booking1.getId()));

        receivedBooking = bookingRepository.findBooking(2L, 1L).get();
        assertThat(receivedBooking.getId(), is(booking1.getId()));

        Optional<Booking> nullBooking = bookingRepository.findBooking(3L, 1L);
        Assertions.assertTrue(nullBooking.isEmpty());
    }
}

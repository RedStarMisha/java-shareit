package ru.practicum.shareit.booking.strategy;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface Strategy {

    BookingState getState();

    List<BookingDto> findBookingByStrategy(Long userId, Pageable page);
}

package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.strategy.BookingState;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDtoEntry bookingDtoEntry);

    BookingDto approveStatus(long userId, long bookingId, boolean approved);

    BookingDto getBookingByUserIdAndBookingId(long userId, long bookingId);

    List<BookingDto> getUserBookingByState(long bookerId, BookingState state, int from, int size);

    List<BookingDto> getBookingForUsersItem(long ownerId, BookingState state, int from, int size);
}

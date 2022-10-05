package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDtoEntry bookingDtoEntry);

    BookingDto approveStatus(long userId, long bookingId, boolean approved);

    BookingDto getBookingByUserIdAndBookingId(long userId, long bookingId);

    List<BookingDto> getUserBookingByState(long bookerId, String state, int from, int size);

    List<BookingDto> getBookingForUsersItem(long ownerId, String state, int from, int size);
}

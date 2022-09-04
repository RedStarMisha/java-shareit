package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.dto.BookingNew;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingNew approveStatus(long userId, long bookingId, boolean approved);

    BookingForResponse getBookingByUserIdAndBookingId(long userId, long bookingId);

    List<BookingForResponse> getUserBookingByState(long bookerId, String state);

    List<BookingForResponse> getBookingForUsersItem(long ownerId, String state);
}

package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.List;

public interface BookingService {

    BookingDtoResponse addBooking(long userId, BookingDtoEntry bookingDtoEntry);

    BookingDtoResponse approveStatus(long userId, long bookingId, boolean approved);

    BookingForResponse getBookingByUserIdAndBookingId(long userId, long bookingId);

    List<BookingForResponse> getUserBookingByState(long bookerId, String state);

    List<BookingForResponse> getBookingForUsersItem(long ownerId, String state);
}

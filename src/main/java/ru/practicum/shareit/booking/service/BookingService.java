package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto approveStatus(long userId, long bookingId, boolean approved);

    BookingDto getBooking(long userId, long bookingId);

    List<BookingDto> getUserBooking(long userId, String state);

    List<BookingDto> getBookingForUsersItem(long userId, String state);
}

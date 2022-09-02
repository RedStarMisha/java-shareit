package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(long userId, BookingDto bookingDto);

    BookingDto approveStatus(long userId, long bookingId, boolean approved);

    BookingDto getBookingByUserIdAndBookingId(long userId, long bookingId);

    List<BookingDto> getUserBookingByState(long bookerId, String state);

    List<BookingDto> getBookingForUsersItem(long ownerId, String state);
}

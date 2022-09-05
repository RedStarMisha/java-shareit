package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static Booking toEntity(User booker, Item item, BookingDtoEntry bookingDtoEntry) {
        Booking booking = new Booking();
        booking.setStart(bookingDtoEntry.getStart());
        booking.setEnd(bookingDtoEntry.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        return booking;
    }

    public static BookingDtoResponse toResponseEntity(Booking booking) {
        return new BookingDtoResponse(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem(),
                booking.getBooker(), booking.getStatus());
    }

    public static BookingShort toBookingShort(Booking booking) {
        return booking != null ? new BookingShort(booking.getId(), booking.getBooker().getId()) : null;
    }
}

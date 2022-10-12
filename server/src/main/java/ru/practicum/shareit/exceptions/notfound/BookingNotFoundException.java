package ru.practicum.shareit.exceptions.notfound;

public class BookingNotFoundException extends EntityNotFoundException {

    public BookingNotFoundException(long bookingId) {
        super(String.format("Бронирование с id = %d не найдено", bookingId));
    }
}

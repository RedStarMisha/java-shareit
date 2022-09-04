package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public interface BookingForResponse {

    long getId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    Item getItem();

    User getBooker();

    BookingStatus getStatus();

}

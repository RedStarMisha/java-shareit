package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.CheckBookingDate;
import ru.practicum.shareit.validation.Create;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@AllArgsConstructor
@Data
@CheckBookingDate(groups = Create.class)
public class BookingDto {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private BookingStatus status;
}

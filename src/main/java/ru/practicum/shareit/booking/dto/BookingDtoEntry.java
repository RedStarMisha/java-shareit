package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.validation.CheckBookingDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@AllArgsConstructor
@Data
@CheckBookingDate
public class BookingDtoEntry {

    private long id;

    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull
    private Long itemId;

    private Long bookerId;

    private BookingStatus status;

}

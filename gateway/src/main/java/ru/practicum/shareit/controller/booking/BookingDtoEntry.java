package ru.practicum.shareit.controller.booking;

import lombok.Value;
import validation.CheckBookingDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Value
@CheckBookingDate
public class BookingDtoEntry {

    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull
    private Long itemId;

}

package ru.practicum.shareit.controller.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import validation.CheckBookingDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@AllArgsConstructor
@Data
@CheckBookingDate
public class BookingDtoEntry {

    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull
    private Long itemId;

}

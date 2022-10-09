package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@AllArgsConstructor
@Data
public class BookingDtoEntry {

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

}

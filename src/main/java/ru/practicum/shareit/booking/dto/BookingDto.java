package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.LocalDateTimeConverter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class BookingDto {

    private Long id;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime start;

    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime end;

    private ItemDtoShort item;

    private User booker;

    private BookingStatus status;
}

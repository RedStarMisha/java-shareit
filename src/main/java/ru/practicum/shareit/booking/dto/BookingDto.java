package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {

    private Long id;

    //убрал чтобы пройти тесты postman
   //@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime start;

    //@JsonFormat(pattern = "yyyy.MM.dd HH:mm")
    private LocalDateTime end;

    private ItemDtoShort item;

    private User booker;

    private BookingStatus status;
}

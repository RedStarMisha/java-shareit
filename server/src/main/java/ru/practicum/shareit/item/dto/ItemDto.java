package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.CommentDto;

import java.util.List;

@Data
public class ItemDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long request;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private BookingShort lastBooking;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private BookingShort nextBooking;

    List<CommentDto> comments;
}


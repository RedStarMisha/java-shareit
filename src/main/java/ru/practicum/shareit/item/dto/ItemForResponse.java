package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.IsText;
import ru.practicum.shareit.validation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemForResponse {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long request;

    private BookingShort lastBooking;

    private BookingShort nextBooking;

    List<CommentDto> comments;
}

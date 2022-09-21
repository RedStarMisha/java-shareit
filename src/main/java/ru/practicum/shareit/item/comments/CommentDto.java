package ru.practicum.shareit.item.comments;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {

    private Long id;

    @NotBlank
    private String text;

    private String authorName;

    private Long itemId;

    private LocalDateTime created;
}

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

    private long itemId;

    private LocalDateTime created;

}

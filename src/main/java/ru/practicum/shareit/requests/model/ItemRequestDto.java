package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Data
public class ItemRequestDto {

    private Long id;

    private String description;

    private Long requestor;

    private Long itemId;

    private LocalDateTime created;

}

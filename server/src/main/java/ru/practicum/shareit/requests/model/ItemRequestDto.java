package ru.practicum.shareit.requests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDtoShort;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Data
public class ItemRequestDto {

    private Long id;

    private String description;

    private Long requestor;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;

    private Set<ItemDtoShort> items;

}

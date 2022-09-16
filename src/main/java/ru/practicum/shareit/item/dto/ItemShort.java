package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ItemShort {
    private Long id;
    private String name;
    private Long ownerId;
}

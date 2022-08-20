package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;

public class RequestMapper {

    public static ItemRequestDto convertToDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getDescription());
    }
}

package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;

public class RequestMapper {

    public static ItemRequestDto convertToDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getDescription());
    }
}

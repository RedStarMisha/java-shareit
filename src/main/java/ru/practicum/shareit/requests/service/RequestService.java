package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequestDto;

import java.util.List;

public interface RequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto);

    ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto);

    void deleteRequest(long userId, long requestId);

    ItemRequestDto getRequest(long userId, long requestId);

    List<ItemRequestDto> getUserRequests(long userId);
}

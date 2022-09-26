package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;

import java.util.List;

public interface RequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDtoEntry itemRequestDto);

    ItemRequestDto getRequest(long userId, long requestId);

    List<ItemRequestDto> getUserRequests(long userId);

    List<ItemRequestDto> getRequests(long userId, Integer from, Integer size);
}

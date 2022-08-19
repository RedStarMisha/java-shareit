package ru.practicum.shareit.requests.storage;

import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;

public interface RequestStorage {

    ItemRequest addRequest(long userId, ItemRequestDto itemRequestDto);

    Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto);

    void deleteRequest(long userId, long requestId);

    Optional<ItemRequest> getRequest(long userId, long requestId);

    List<ItemRequest> getUserRequests(long userId);
}

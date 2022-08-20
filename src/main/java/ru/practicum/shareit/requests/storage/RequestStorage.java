package ru.practicum.shareit.requests.storage;

import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;

import java.util.List;
import java.util.Optional;

public interface RequestStorage {

    ItemRequest addRequest(long userId, ItemRequestDto itemRequestDto);

    Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto);

    void deleteRequest(long userId, long requestId);

    Optional<ItemRequest> getRequest(long userId, long requestId);

    List<ItemRequest> getUserRequests(long userId);
}

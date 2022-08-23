package ru.practicum.shareit.requests.storage;

import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface RequestStorage {

    ItemRequest addRequest(long userId, ItemRequest itemRequest);

    Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequest itemRequest);

    void deleteRequest(long userId, long requestId);

    Optional<ItemRequest> getRequest(long userId, long requestId);

    List<ItemRequest> getUserRequests(long userId);
}

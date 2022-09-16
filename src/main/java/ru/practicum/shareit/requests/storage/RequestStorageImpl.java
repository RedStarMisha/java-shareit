package ru.practicum.shareit.requests.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class RequestStorageImpl implements RequestStorage {
    private final Map<Long, Map<Long, ItemRequest>> storage = new HashMap<>();

    @Override
    public ItemRequest addRequest(long userId, ItemRequest itemRequest) {
        if (storage.containsKey(userId)) {
            storage.get(userId).put(itemRequest.getId(), itemRequest);
        } else {
            storage.put(userId, Map.of(itemRequest.getId(), itemRequest));
        }
        log.info("Запрос {} добавлен", storage.get(userId).get(itemRequest.getId()));
        return itemRequest;
    }

    @Override
    public Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequest itemRequest) {
        if (storage.get(userId).containsKey(requestId)) {
            if (itemRequest.getDescription() != null) {
                storage.get(userId).get(requestId).setDescription(itemRequest.getDescription());
            }
            log.info("запрос {} обновлен", storage.get(userId).get(requestId));
            return Optional.of(storage.get(userId).get(requestId));
        }
        log.warn("запрос с id = {} отсутсвует", requestId);
        return Optional.empty();
    }

    @Override
    public void deleteRequest(long userId, long requestId) {
        storage.get(userId).remove(requestId);
        log.info("запрос с id = {} удален", requestId);
    }

    @Override
    public Optional<ItemRequest> getRequest(Long requestId) {
        if (requestId != null) {
            log.info("запрос {} отправлен", requestId);
            return storage.values()
                    .stream()
                    .map(Map::values)
                    .flatMap(itemRequests -> itemRequests.stream()).filter(itemRequest -> itemRequest.getId() == requestId)
                    .findFirst();
        }
        log.warn("запрос с id = {} не найден", requestId);
        return Optional.empty();
    }

    @Override
    public List<ItemRequest> getUserRequests(long userId) {
        if (storage.containsKey(userId)) {
            log.info("список запросов пользователя {} отправлен", userId);
            return new ArrayList<>(storage.get(userId).values());
        }
        log.info("список запросов пользователя {} пуст", userId);
        return null;
    }
}

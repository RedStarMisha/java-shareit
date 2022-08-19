package ru.practicum.shareit.requests.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.util.*;

@Slf4j
@Repository
public class RequestStorageImpl implements RequestStorage{

    private final Map<Long, Map<Long, ItemRequest>> storage = new HashMap<>();
    private long requestId = 1;

    @Override
    public ItemRequest addRequest(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest(requestId++, userId, itemRequestDto);
        if (storage.containsKey(userId)) {
            storage.get(userId).put(itemRequest.getId(), itemRequest);
        } else {
            storage.put(userId, Map.of(itemRequest.getId(), itemRequest));
        }
        log.info("Запрос {} добавлен", storage.get(userId).get(itemRequest.getId()));
        return itemRequest;
    }

    @Override
    public Optional<ItemRequest> updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto) {
        if (storage.get(userId).containsKey(requestId)) {
            if (itemRequestDto.getDescription() != null) {
                storage.get(userId).get(requestId).setDescription(itemRequestDto.getDescription());
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
    public Optional<ItemRequest> getRequest(long userId, long requestId) {
        if (storage.get(userId).containsKey(requestId)) {
            log.info("запрос {} отправлен", storage.get(userId).get(requestId));
            return Optional.of(storage.get(userId).get(requestId));
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

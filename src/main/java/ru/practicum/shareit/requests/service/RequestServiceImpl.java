package ru.practicum.shareit.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.requests.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.storage.UserStorageImpl;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.requests.RequestMapper.toRequest;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestStorage requestStorage;
    private final UserStorage userStorage;
    private long requestId = 1;

    @Autowired
    public RequestServiceImpl(RequestStorage requestStorage, UserStorageImpl userStorage) {
        this.requestStorage = requestStorage;
        this.userStorage = userStorage;
    }

    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDtoEntry itemRequestDto) {
        User user = userStorage.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        ItemRequest itemRequest = toRequest(user, itemRequestDto);
        itemRequest.setId(requestId++);
//        return toRequestDto(requestStorage.addRequest(userId, ));
        return null;
    }

    @Override
    public ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDtoEntry itemRequestDto) {
        User user = userStorage.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        return requestStorage.updateRequest(userId, requestId, toRequest(user, itemRequestDto))
                .map(RequestMapper::toRequestDto).orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public void deleteRequest(long userId, long requestId) {
        User user = userStorage.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        requestStorage.getRequest(requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        requestStorage.deleteRequest(userId, requestId);
    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        userStorage.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        return requestStorage.getRequest(requestId).map(RequestMapper::toRequestDto)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        User user = userStorage.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        return requestStorage.getUserRequests(userId).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getRequests(long userId, Integer from, Integer size) {
        return null;
    }
}

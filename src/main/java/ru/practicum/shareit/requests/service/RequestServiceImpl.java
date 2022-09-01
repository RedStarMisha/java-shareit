package ru.practicum.shareit.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.requests.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.requests.RequestMapper.toRequest;
import static ru.practicum.shareit.requests.RequestMapper.toRequestDto;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestStorage requestStorage;
    private final UserService userService;
    private long requestId = 1;

    @Autowired
    public RequestServiceImpl(RequestStorage requestStorage, @Qualifier("storage") UserService userService) {
        this.requestStorage = requestStorage;
        this.userService = userService;
    }

    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) {
        userService.getUserById(userId);
        itemRequestDto.setId(requestId++);
        return toRequestDto(requestStorage.addRequest(userId, toRequest(userId, itemRequestDto)));
    }

    @Override
    public ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto) {
        userService.getUserById(userId);
        return requestStorage.updateRequest(userId, requestId, toRequest(userId, itemRequestDto))
                .map(RequestMapper::toRequestDto).orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public void deleteRequest(long userId, long requestId) {
        userService.getUserById(userId);
        requestStorage.getRequest(userId, requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        requestStorage.deleteRequest(userId, requestId);
    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        userService.getUserById(userId);
        return requestStorage.getRequest(userId, requestId).map(RequestMapper::toRequestDto)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        userService.getUserById(userId);
        return requestStorage.getUserRequests(userId).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }
}

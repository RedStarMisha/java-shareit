package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestStorage requestStorage;
    private final UserService userService;

    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) throws UserNotFoundException {
        userService.getUserById(userId);
        return RequestMapper.convertToDto(requestStorage.addRequest(userId, itemRequestDto));
    }

    @Override
    public ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto)
            throws RequestNotFoundException, UserNotFoundException {
        userService.getUserById(userId);
        return requestStorage.updateRequest(userId, requestId, itemRequestDto).map(RequestMapper::convertToDto)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public void deleteRequest(long userId, long requestId) throws RequestNotFoundException, UserNotFoundException {
        userService.getUserById(userId);
        requestStorage.getRequest(userId, requestId).orElseThrow(() -> new RequestNotFoundException(requestId));
        requestStorage.deleteRequest(userId, requestId);
    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) throws RequestNotFoundException, UserNotFoundException {
        userService.getUserById(userId);
        return requestStorage.getRequest(userId, requestId).map(RequestMapper::convertToDto)
                .orElseThrow(() -> new RequestNotFoundException(requestId));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) throws UserNotFoundException {
        userService.getUserById(userId);
        return requestStorage.getUserRequests(userId).stream()
                .map(RequestMapper::convertToDto).collect(Collectors.toList());
    }
}

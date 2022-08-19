package ru.practicum.shareit.requests;

import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;

public interface RequestService {

    ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) throws UserNotFoundException;

    ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto) throws RequestNotFoundException, UserNotFoundException;

    void deleteRequest(long userId, long requestId) throws RequestNotFoundException, UserNotFoundException;

    ItemRequestDto getRequest(long userId, long requestId) throws RequestNotFoundException, UserNotFoundException;

    List<ItemRequestDto> getUserRequests(long userId) throws UserNotFoundException;
}

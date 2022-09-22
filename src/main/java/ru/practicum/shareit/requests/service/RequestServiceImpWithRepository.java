package ru.practicum.shareit.requests.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.model.ItemRequestDto;

import java.util.List;

@Service
@Qualifier("repository")
public class RequestServiceImpWithRepository implements RequestService {
    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDto itemRequestDto) {
        return null;
    }

    @Override
    public ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDto itemRequestDto) {
        return null;
    }

    @Override
    public void deleteRequest(long userId, long requestId) {

    }

    @Override
    public ItemRequestDto getRequest(long userId, long requestId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        return null;
    }
}

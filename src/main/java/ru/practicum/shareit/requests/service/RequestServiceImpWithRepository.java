package ru.practicum.shareit.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.requests.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@Qualifier("repository")
@AllArgsConstructor(onConstructor_ =@Autowired)
public class RequestServiceImpWithRepository implements RequestService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;
    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDtoEntry itemRequestDtoEntry) {
        User requestor = userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        ItemRequest itemRequest = requestRepository.save(RequestMapper.toRequest(requestor, itemRequestDtoEntry));
        return RequestMapper.toRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDtoEntry itemRequestDto) {
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

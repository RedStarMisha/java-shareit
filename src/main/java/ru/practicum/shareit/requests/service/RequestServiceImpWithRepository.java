package ru.practicum.shareit.requests.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.requests.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
import ru.practicum.shareit.requests.storage.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Qualifier("repository")
@AllArgsConstructor(onConstructor_ =@Autowired)
public class RequestServiceImpWithRepository implements RequestService {

    private final UserRepository userRepository;

    private final RequestRepository requestRepository;
    @Override
    public ItemRequestDto addRequest(long userId, ItemRequestDtoEntry itemRequestDtoEntry) {
        User requestor = checkUser(userId);
        log.info("Проверку прошло");
        ItemRequest itemRequest = requestRepository.save(RequestMapper.toRequest(requestor, itemRequestDtoEntry));
        return RequestMapper.toRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequest(long requestId) {
        return requestRepository.findById(requestId)
                .map(itemRequest -> RequestMapper.toRequestDto(itemRequest))
                .orElseThrow(()-> new RequestNotFoundException(requestId));
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        checkUser(userId);
        return requestRepository.findAllByRequestor_IdOrderByCreatedDesc(userId).stream()
                .map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getRequests(Integer from, Integer size) {
        return requestRepository.findAll(makePageParam(from, size)).stream().map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto updateRequest(long userId, long requestId, ItemRequestDtoEntry itemRequestDto) {
        return null;
    }

    @Override
    public void deleteRequest(long userId, long requestId) {

    }

    private User checkUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
    }

    private Pageable makePageParam(int from, int size) {
        Sort sort = Sort.by("created").descending();
        return PageRequest.of(from, size, sort);
    }
}

package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.service.RequestService;

import javax.validation.Valid;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")

public class ItemRequestController {

    private final RequestService requestService;

    @Autowired
    public ItemRequestController(@Qualifier("repository") RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Later-User-Id") Long userId,
                                     @Valid ItemRequestDto itemRequestDto) {
        return requestService.addRequest(userId, itemRequestDto);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto updateRequest(@RequestHeader("X-Later-User-Id") long userId, @PathVariable Long requestId,
                                        @Valid ItemRequestDto itemRequestDto) {
        return requestService.updateRequest(userId, requestId, itemRequestDto);
    }

    @DeleteMapping("/{requestId}")
    public void deleteRequest(@RequestHeader("X-Later-User-Id") long userId, @PathVariable long requestId) {
        requestService.deleteRequest(userId, requestId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Later-User-Id") long userId, @PathVariable long requestId) {
        return requestService.getRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Later-User-Id") long userId) {
        return requestService.getUserRequests(userId);
    }
}

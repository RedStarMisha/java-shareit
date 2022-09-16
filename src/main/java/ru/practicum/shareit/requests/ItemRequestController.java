package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequestDtoEntry;
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
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody @Valid ItemRequestDtoEntry itemRequestDtoEntry) {
        return requestService.addRequest(userId, itemRequestDtoEntry);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestService.getUserRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return requestService.getRequests(from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return requestService.getRequest(requestId);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto updateRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long requestId,
                                             @Valid ItemRequestDtoEntry itemRequestDto) {
        return requestService.updateRequest(userId, requestId, itemRequestDto);
    }

    @DeleteMapping("/{requestId}")
    public void deleteRequest(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        requestService.deleteRequest(userId, requestId);
    }
}

package ru.practicum.shareit.requests;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.RequestNotFoundException;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto addRequest(@RequestHeader("X-Later-User-Id") Long userId, ItemRequestDto itemRequestDto)
            throws UserNotFoundException {
        return requestService.addRequest(userId, itemRequestDto);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto updateRequest(@RequestHeader("X-Later-User-Id") long userId, @PathVariable Long requestId,
                            ItemRequestDto itemRequestDto) throws UserNotFoundException, RequestNotFoundException {
        return requestService.updateRequest(userId, requestId, itemRequestDto);
    }

    @DeleteMapping("/{requestId}")
    public void deleteRequest(@RequestHeader("X-Later-User-Id") long userId, @PathVariable long requestId)
            throws UserNotFoundException, RequestNotFoundException {
        requestService.deleteRequest(userId, requestId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequest(@RequestHeader("X-Later-User-Id") long userId, @PathVariable long requestId)
            throws UserNotFoundException, RequestNotFoundException {
        return requestService.getRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getUserRequests(@RequestHeader("X-Later-User-Id") long userId)
            throws UserNotFoundException {
        return requestService.getUserRequests(userId);
    }
}

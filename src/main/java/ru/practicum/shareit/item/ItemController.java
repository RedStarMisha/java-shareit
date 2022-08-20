package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.exceptions.UserNotFoundException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;
    @PostMapping
    ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                    @RequestBody @Valid ItemDto item) throws UserNotFoundException {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ItemDto editItem (@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                      @RequestBody ItemDto item) throws ItemNotFoundException, UserNotFoundException {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                 @PathVariable(name = "itemId") long itemId) throws ItemNotFoundException, UserNotFoundException {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId) throws UserNotFoundException {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    List<ItemDto> findItemByName(@RequestHeader("X-Sharer-User-Id") long userId,
                        @RequestParam(name = "text") String text) {
        return text.isBlank() ? new ArrayList<>() : itemService.findItemByName(text);
    }
}

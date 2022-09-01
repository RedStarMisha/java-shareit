package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validation.Update;

import java.util.Collections;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    @Autowired
    public ItemController(@Qualifier("repository") ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                    @RequestBody @Validated(Create.class) ItemDto item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ItemDto editItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                     @RequestBody @Validated(Update.class) ItemDto item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                    @PathVariable(name = "itemId") long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    List<ItemDto> findItemByName(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestParam(name = "text") String text) {
        return text.isBlank() ? Collections.emptyList() : itemService.findItemByName(text);
    }
}

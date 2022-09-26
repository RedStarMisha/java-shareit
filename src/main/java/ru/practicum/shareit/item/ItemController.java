package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
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
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    ItemDtoShort addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                         @RequestBody @Validated(Create.class) ItemDtoShort item) {
        return itemService.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ItemDtoShort updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                            @RequestBody @Validated(Update.class) ItemDtoShort item) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                    @PathVariable(name = "itemId") long itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemService.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    List<ItemDtoShort> findItemsByName(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @RequestParam(name = "text") String text,
                                       @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return text.isBlank() ? Collections.emptyList() : itemService.findItemByName(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long authorId, @PathVariable(name = "itemId") Long itemId,
                          @RequestBody @Valid CommentDto commentDto) {
        return itemService.addComment(authorId, itemId, commentDto);
    }
}

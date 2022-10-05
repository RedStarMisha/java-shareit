package ru.practicum.shareit.controller.item;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.validation.Create;
import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@AllArgsConstructor(onConstructor_ = @Autowired)
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                   @RequestBody @Validated(Create.class) ItemDtoShort item) {
        return itemClient.addItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                            @RequestBody @Validated(Update.class) ItemDtoShort item) {
        return itemClient.updateItem(userId, itemId, item);
    }

    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                    @PathVariable(name = "itemId") long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    ResponseEntity<Object> getOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.getOwnerItems(userId, from, size);
    }

    @GetMapping("/search")
    ResponseEntity<Object> findItemsByName(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @NotBlank @RequestParam(name = "text") String text,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return itemClient.findItemsByName(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long authorId, @PathVariable(name = "itemId") Long itemId,
                          @RequestBody @Valid CommentDto commentDto) {
        return itemClient.addComment(authorId, itemId, commentDto);
    }

}
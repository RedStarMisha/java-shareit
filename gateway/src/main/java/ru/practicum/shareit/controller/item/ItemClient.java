package ru.practicum.shareit.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.controller.client.BaseClient;
import ru.practicum.shareit.exceptions.CommentCreationException;
import ru.practicum.shareit.exceptions.PaginationParametersException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.RequestNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;

import ru.practicum.shareit.validation.Update;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))  //фабрика для построения URI
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)  //фабрика для создания HTTPRequest
                        .build()
        );
    }

    ResponseEntity<Object> addItem(long userId, ItemDtoShort item) {
        return post("", userId, item);
    }

    ResponseEntity<Object> updateItem(long userId, long itemId, ItemDtoShort item) {
        return patch("/" + itemId, userId, item);
    }

    ResponseEntity<Object> getItem(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    ResponseEntity<Object> getOwnerItems(long userId,
                                @RequestParam(name = "from", defaultValue = "0") Integer from,
                                @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("", userId, parameters);
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

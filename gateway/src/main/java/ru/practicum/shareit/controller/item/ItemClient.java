package ru.practicum.shareit.controller.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.controller.client.BaseClient;

import java.util.Map;


public class ItemClient extends BaseClient {

    public ItemClient(RestTemplate rest) {
        super(rest);
    }

    ResponseEntity<Object> addItem(long userId, ItemDtoShort item) {
        return post("", userId, item);
    }

    ResponseEntity<Object> updateItem(long userId, long itemId, ItemDtoShort item) {
        return patch("/" + itemId, userId, item);
    }

    ResponseEntity<Object> getItemById(long userId, long itemId) {
        return get("/" + itemId, userId);
    }

    ResponseEntity<Object> getOwnerItems(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    ResponseEntity<Object> findItemsByName(long userId, String text, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", userId, parameters);
    }

    ResponseEntity<Object> addComment(long authorId, long itemId, CommentDto commentDto) {
        return post("/" + itemId + "/comment", authorId, commentDto);
    }
}

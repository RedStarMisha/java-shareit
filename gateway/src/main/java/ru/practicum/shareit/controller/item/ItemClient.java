package ru.practicum.shareit.controller.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.controller.client.BaseClient;

import java.util.Map;


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

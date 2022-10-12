package ru.practicum.shareit.controller.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.controller.client.BaseClient;

import java.util.Map;

public class RequestClient extends BaseClient {

    public RequestClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> addRequest(long userId, ItemRequestDtoEntry itemRequestDtoEntry) {
        return post("", userId, itemRequestDtoEntry);
    }

    public ResponseEntity<Object> getRequest(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getUserRequests(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequests(long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", userId, parameters);
    }
}

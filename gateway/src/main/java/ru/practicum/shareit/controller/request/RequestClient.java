package ru.practicum.shareit.controller.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.controller.client.BaseClient;

import java.util.Map;

public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))  //фабрика для построения URI
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)  //фабрика для создания HTTPRequest
                        .build()
        );
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

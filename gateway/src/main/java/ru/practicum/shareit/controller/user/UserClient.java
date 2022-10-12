package ru.practicum.shareit.controller.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.controller.client.BaseClient;

@Slf4j
public class UserClient extends BaseClient {

    public UserClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> addUser(UserDto userDto) {
        log.info(rest.getUriTemplateHandler().toString());
        return post("", userDto);
    }

    public ResponseEntity<Object> updateUser(long userId, UserDto userDto) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> getUser(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> deleteUser(long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }
}

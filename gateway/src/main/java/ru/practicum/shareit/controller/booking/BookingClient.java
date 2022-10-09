package ru.practicum.shareit.controller.booking;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
            builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))  //фабрика для построения URI
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)  //фабрика для создания HTTPRequest
                .build()
        );
    }

    public ResponseEntity<Object> addBooking(long userId, BookingDtoEntry requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> approveStatus(long userId, long bookingId, boolean approved) {
        log.info("Approve booking status by userId={} for bookingId={}", userId, bookingId);
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    ResponseEntity<Object> getBookingByOwner(long ownerId, BookingState state, int from, int size) {
        log.info("Get booking with state {}, ownerId={}, from={}, size={}", state, ownerId, from, size);
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", ownerId, parameters);
    }
}

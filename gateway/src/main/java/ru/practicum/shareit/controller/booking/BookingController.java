package ru.practicum.shareit.controller.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.controller.exceptions.UnknownBookingStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;


	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long bookerId,
											 @RequestBody @Valid BookingDtoEntry requestDto) {
		return bookingClient.addBooking(bookerId, requestDto);
	}

	@PatchMapping("/{bookingId}")
	@CachePut(value = "bookings", key = "#bookingId")
	public ResponseEntity<Object> approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @PathVariable(name = "bookingId") Long bookingId,
										 @RequestParam(name = "approved") Boolean approved) {
		return bookingClient.approveStatus(userId, bookingId, approved);
	}

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long bookerId,
										  @RequestParam(name = "state", defaultValue = "all") String stateParam,
										  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
										  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnknownBookingStateException(stateParam));
		return bookingClient.getBookings(bookerId, state, from, size);
	}

	@GetMapping("/{bookingId}")
	@Cacheable(value = "bookings", key = "{#bookingId, #userId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
								@RequestParam(name = "state", defaultValue = "ALL", required = false) String stateParam,
								@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
								@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new UnknownBookingStateException(stateParam));
		return bookingClient.getBookingByOwner(userId, state, from, size);
	}
}

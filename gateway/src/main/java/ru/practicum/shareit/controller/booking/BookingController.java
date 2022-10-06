package ru.practicum.shareit.controller.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @RequestBody @Valid BookingDtoEntry requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.addBooking(userId, requestDto);
	}

	@PatchMapping("/{bookingId}")
	ResponseEntity<Object> approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
										 @PathVariable(name = "bookingId") Long bookingId,
										 @RequestParam(name = "approved") Boolean approved) {
		log.info("Approve booking status by userId={} for bookingId={}", userId, bookingId);
		return bookingClient.approveStatus(userId, bookingId, approved);
	}

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
										  @RequestParam(name = "state", defaultValue = "all") String stateParam,
										  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
										  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping("/owner")
	ResponseEntity<Object> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
								@RequestParam(name = "state", defaultValue = "ALL", required = false) String stateParam,
								@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
								@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, ownerId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookingByOwner(userId, state, from, size);
	}
}

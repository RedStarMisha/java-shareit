package ru.practicum.shareit.controller.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

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

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
										   @RequestBody @Valid BookingDtoEntry requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
											 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	ResponseEntity<Object> approveStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
							 @PathVariable(name = "bookingId") Long bookingId,
							 @RequestParam(name = "approved") Boolean approved) {
		return bookingClient.approveStatus(userId, bookingId, approved);
	}

	@GetMapping("/owner")
	ResponseEntity<Object> getBookingForUsersItem(@RequestHeader("X-Sharer-User-Id") Long userId,
											@RequestParam(name = "state", defaultValue = "ALL", required = false) String state,
											@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
											@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		return bookingClient.getBookingForUsersItem(userId, state, from, size);
	}
}
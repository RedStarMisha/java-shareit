package ru.practicum.shareit.booking.strategy;

import java.util.Optional;

public enum BookingState {
    CURRENT,
    PAST,
    FUTURE,
    ALL,
    WAITING,
    REJECTED;

    public static Optional<BookingState> from(String stringState) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}

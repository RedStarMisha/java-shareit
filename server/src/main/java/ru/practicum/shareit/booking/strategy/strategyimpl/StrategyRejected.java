package ru.practicum.shareit.booking.strategy.strategyimpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.strategy.BookingState;
import ru.practicum.shareit.booking.strategy.Strategy;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class StrategyRejected implements Strategy {
    private final BookingRepository bookingRepository;

    @Override
    public BookingState getState() {
        return BookingState.REJECTED;
    }

    @Override
    public List<BookingDto> findBookingByStrategy(Long bookerId, Pageable page) {
        return bookingRepository.findBookingsByBooker_IdAndStatus(bookerId, BookingStatus.REJECTED, page)
                .stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}

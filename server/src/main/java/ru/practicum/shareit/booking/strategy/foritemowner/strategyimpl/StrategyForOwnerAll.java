package ru.practicum.shareit.booking.strategy.foritemowner.strategyimpl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.strategy.BookingState;
import ru.practicum.shareit.booking.strategy.foritemowner.StrategyForItemOwner;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class StrategyForOwnerAll implements StrategyForItemOwner {
    private final BookingRepository bookingRepository;

    @Override
    public BookingState getState() {
        return BookingState.ALL;
    }

    @Override
    public List<BookingDto> findBookingByStrategy(Long ownerId, Pageable page) {
        return bookingRepository.findBookingsByItem_Owner_Id(ownerId, page).stream()
                .map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}

package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.strategy.BookingState;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.strategy.forbooker.StrategyForBooker;
import ru.practicum.shareit.booking.strategy.forbooker.StrategyForBookerFactory;
import ru.practicum.shareit.booking.strategy.foritemowner.StrategyForItemOwner;
import ru.practicum.shareit.booking.strategy.foritemowner.StrategyForItemOwnerFactory;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.exceptions.notfound.BookingNotFoundException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final StrategyForBookerFactory strategyForBookerFactory;
    private final StrategyForItemOwnerFactory strategyForItemOwnerFactory;

    @Override
    public BookingDto addBooking(long userId, BookingDtoEntry bookingDtoEntry) {
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(bookingDtoEntry.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDtoEntry.getItemId()));
        if (item.getOwner().getId() == userId) {
            throw new BookingCreationException("Пользователь не может арендовать вещь сам у себя");
        }
        if (item.getAvailable().equals(false)) {
            throw new ItemAvailableException(item.getId());
        }
        Booking booking = BookingMapper.toBooking(booker, item, bookingDtoEntry);
        booking = bookingRepository.save(booking);
        log.info("Бронирование добавлено " + booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto approveStatus(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findByIdAndItem_Owner_Id(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingStatusException("У аренды нельзя изменить статус");
        }
        BookingStatus bookingStatus = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        booking.setStatus(bookingStatus);
        bookingRepository.save(booking);
        log.warn("Бронирование обновлено " + booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingByUserIdAndBookingId(long userId, long bookingId) {
        return bookingRepository.findBooking(userId, bookingId).map(BookingMapper::toBookingDto)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public List<BookingDto> getBookingByStateAndBookerId(long bookerId, BookingState state, int from, int size) {
        userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException(bookerId));
        StrategyForBooker strategy = strategyForBookerFactory.findStrategy(state);
        return strategy.findBookingByStrategy(bookerId, makePageParam(from, size));
    }

    @Override
    public List<BookingDto> getBookingByStateAndItemsOwner(long ownerId, BookingState state, int from, int size) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        StrategyForItemOwner strategy = strategyForItemOwnerFactory.findStrategy(state);
        return strategy.findBookingByStrategy(ownerId, makePageParam(from, size));
    }

    public static Pageable makePageParam(int from, int size) {
        int page = from / size;
        Sort sort = Sort.by("start").descending();
        return PageRequest.of(page, size, sort);
    }
}

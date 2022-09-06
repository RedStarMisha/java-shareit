package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.*;
import ru.practicum.shareit.exceptions.notfound.BookingNotFoundException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;

@Service
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository,
                              ItemRepository itemRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    @Transactional
    public BookingDto addBooking(long userId, BookingDtoEntry bookingDtoEntry) {
        log.warn("Мой ДТО {}", bookingDtoEntry);
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(bookingDtoEntry.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDtoEntry.getItemId()));
        if (item.getId() == userId) {
            throw new BookingCreationException("Пользователь не может арендовать вещь сам у себя");
        }
        if (item.getAvailable().equals(false)) {
            throw new ItemAvailableException(item.getId());
        }
        Booking booking = BookingMapper.toBooking(booker, item, bookingDtoEntry);
        booking = bookingRepository.save(booking);
        log.info("Бронирование добавлено " + booking);
        return toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveStatus(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findByIdAndItem_Owner_Id(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        BookingStatus bookingStatus = approved == true ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingStatusException(bookingStatus.name());
        }
        booking.setStatus(bookingStatus);
        booking = bookingRepository.save(booking);
        log.warn("Бронирование обновлено " + booking);
        return toBookingDto(booking);
    }

    @Override
    public BookingDto getBookingByUserIdAndBookingId(long userId, long bookingId) {
        return bookingRepository.findBooking(userId, bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }



    @Override
    public List<BookingDto> getUserBookingByState(long bookerId, String state) {
        userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException(bookerId));
        List<BookingDto> list = bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId);
        return filterByState(list, state);
    }

    @Override
    public List<BookingDto> getBookingForUsersItem(long ownerId, String state) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        List<BookingDto> list = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(ownerId);
        return filterByState(list, state);
    }

    @Override
    public BookingState saveState(String s) {
        try {
            return BookingState.valueOf(s);
        } catch (Throwable e) {
            throw new UnknownBookingStateException(s);
        }
    }

    private List<BookingDto> filterByState(List<BookingDto> list, String state) {
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (Throwable e) {
            throw new UnknownBookingStateException(state);
        }
        Predicate<BookingDto> filter = (booking -> true);
        switch (bookingState) {
            case CURRENT:
                filter = (b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()));
                break;
            case PAST:
                filter = (b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isBefore(LocalDateTime.now()));
                break;
            case FUTURE:
                filter = (b -> b.getStart().isAfter(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()));
                break;
            case WAITING:
            case REJECTED:
                filter = (booking -> booking.getStatus().name().equals(state));
                break;
        }
        return list.stream().filter(filter).collect(Collectors.toList());
    }
}

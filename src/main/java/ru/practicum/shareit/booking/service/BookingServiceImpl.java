package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingMapper.toResponseEntity;

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
    public BookingDtoResponse addBooking(long userId, BookingDtoEntry bookingDtoEntry) {
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(bookingDtoEntry.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDtoEntry.getItemId()));
        if (item.getId() == userId) {
            throw new BookingCreationException("Пользователь не может арендовать вещь сам у себя");
        }
        if (item.getAvailable().equals(false)) {
            throw new ItemAvailableException(item.getId());
        }
        Booking booking = BookingMapper.toEntity(booker, item, bookingDtoEntry);
        booking = bookingRepository.save(booking);
        log.info("Бронирование добавлено " + booking);
        return toResponseEntity(booking);
    }

    @Override
    public BookingDtoResponse approveStatus(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findByIdAndItem_Owner_Id(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        BookingStatus bookingStatus = approved == true ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingStatusException(bookingStatus.name());
        }
        booking.setStatus(bookingStatus);
        booking = bookingRepository.save(booking);
        log.warn("Бронирование обновлено " + booking);
        return toResponseEntity(booking);
    }

    @Override
    public BookingForResponse getBookingByUserIdAndBookingId(long userId, long bookingId) {
        return bookingRepository.findBookingNew(userId, bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Override
    public List<BookingForResponse> getUserBookingByState(long bookerId, String state) {
        userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException(bookerId));
        List<BookingForResponse> list = bookingRepository.findAllByBooker_IdOrderByStartDesc(bookerId);
        return filterByState(list, state);
    }

    @Override
    public List<BookingForResponse> getBookingForUsersItem(long ownerId, String state) {
        userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException(ownerId));
        List<Item> userItems = itemRepository.findAllByOwner_Id(ownerId);
        if (userItems.isEmpty()) {
            return Collections.emptyList();
        }
        List<BookingForResponse> list = bookingRepository.findAllByItem_Owner_IdOrderByStartDesc(ownerId);
        return filterByState(list, state);
    }

    private List<BookingForResponse> filterByState(List<BookingForResponse> list, String state) {
        if (state.equals(BookingStatus.WAITING.name()) || state.equals(BookingStatus.REJECTED.name())) {
            return list.stream().filter(booking -> booking.getStatus().name().equals(state))
                    .collect(Collectors.toList());
        } else if (state.equals("CURRENT")){
            return list.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now())
                    && b.getEnd().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            return list.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now())
                    && b.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            return list.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now())
                    && b.getEnd().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        } else if (state.equals("ALL")) {
            return list;
        } else {
            throw new UnknownBookingStateException(state);
        }
    }
}

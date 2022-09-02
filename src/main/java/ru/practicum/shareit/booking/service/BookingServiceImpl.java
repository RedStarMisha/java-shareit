package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exceptions.BookingCreationException;
import ru.practicum.shareit.exceptions.notfound.BookingNotFoundException;
import ru.practicum.shareit.exceptions.BookingStatusException;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
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
    public BookingDto addBooking(long userId, BookingDto bookingDto) {
        User booker = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Item item = itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException(bookingDto.getItemId()));
        if (item.getId() == userId || item.getAvailable().equals(false)) {
            throw new BookingCreationException("Пользователь не может арендовать эту вещь");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingCreationException("Дата начала аренды уже прошла");
        }
        Booking booking = Mapper.toEntity(booker, item, bookingDto);
        return Mapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto approveStatus(long userId, long bookingId, boolean approved) {
        Booking booking = bookingRepository.findByIdAndItem_Owner_Id(bookingId, userId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        BookingStatus bookingStatus = approved == true ? BookingStatus.APPROVED : BookingStatus.REJECTED;
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingStatusException(bookingStatus.name());
        }
        return Mapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingByUserIdAndBookingId(long userId, long bookingId) {
        Booking booking = bookingRepository.findBookingByIdAndUser(userId, bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        return Mapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookingByState(long bookerId, String state) {
        List<Booking> list = bookingRepository.findAllByBooker_IdOrderByStartAsc(bookerId);
        return filterByState(list, state);
    }

    @Override
    public List<BookingDto> getBookingForUsersItem(long ownerId, String state) {
        List<Item> userItems = itemRepository.findAllByOwner_Id(ownerId);
        if (userItems.isEmpty()) {
            return null;
        }
        List<Booking> list = bookingRepository.findAllByItem_Owner_IdOrderByStartAsc(ownerId);
        return filterByState(list, state);
    }

    private List<BookingDto> filterByState(List<Booking> list, String state) {
        if (state.equals(BookingStatus.WAITING.name()) || state.equals(BookingStatus.REJECTED.name())) {
            return list.stream().filter(booking -> booking.getStatus().name().equals(state)).map(Mapper::toDto)
                    .collect(Collectors.toList());
        } else if (state.equals("CURRENT")){
            return list.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now())
                    && b.getEnd().isAfter(LocalDateTime.now())).map(Mapper::toDto).collect(Collectors.toList());
        } else if (state.equals("PAST")) {
            return list.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now())
                    && b.getEnd().isBefore(LocalDateTime.now())).map(Mapper::toDto).collect(Collectors.toList());
        } else if (state.equals("FUTURE")) {
            return list.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now())
                    && b.getEnd().isAfter(LocalDateTime.now())).map(Mapper::toDto).collect(Collectors.toList());
        } else {
            return list.stream().map(Mapper::toDto).collect(Collectors.toList());
        }
    }
}

package ru.practicum.shareit.booking.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

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
        return null;
    }

    @Override
    public BookingDto approveStatus(long userId, long bookingId, boolean approved) {
        return null;
    }

    @Override
    public BookingDto getBooking(long userId, long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getUserBooking(long userId, String state) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingForUsersItem(long userId, String state) {
        return null;
    }
}

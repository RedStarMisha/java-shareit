package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getAvailable(),
                item.getRequest() == null ? null : item.getRequest().getId());
    }

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem().getId(),
                booking.getBooker().getId(), booking.getStatus(), booking.getItem().getName());
    }

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static Item toEntity(User owner, ItemDto itemDto, @Nullable ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(owner);
        item.setRequest(itemRequest);
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static Booking toEntity(User booker, Item item, BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBooker(booker);
        booking.setItem(item);
        return booking;
    }

    public static User updateFromDto(User user, UserDto userDto) {
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() !=null) {
            user.setName(userDto.getName());
        }
        return user;
    }

    public static Item updateFromDto(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }



}

package server;

import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoEntry;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.model.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TestUtil {

    public static ItemRequest makeItemRequest(Long id, String description, User requestor, Set<Item> items) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        itemRequest.setDescription(description);
        itemRequest.setRequestor(requestor);
        itemRequest.setItems(items);
        return itemRequest;
    }

    public static ItemRequestDto makeItemRequestDto(Long id, String description, Long requestor, LocalDateTime date,
                                                    Set<ItemDtoShort> items) {
        return new ItemRequestDto(id, description, requestor, date, items);
    }

    public static User makeUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public static Item makeItem(Long itemId, String name, String description, User owner, Boolean available,
                                ItemRequest itemRequest) {
        Item item = new Item();
        item.setId(itemId);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(owner);
        item.setRequest(itemRequest);
        return item;
    }

    public static ItemDtoShort makeItemDtoShort(Long id, String name, String description, Boolean available, @Nullable Long requestId) {
        ItemDtoShort itemDtoShort = new ItemDtoShort();
        itemDtoShort.setId(id);
        itemDtoShort.setName(name);
        itemDtoShort.setDescription(description);
        itemDtoShort.setAvailable(available);
        itemDtoShort.setRequestId(requestId);
        return itemDtoShort;
    }

    public static ItemDto makeItemDto(Long id, String name, String description, Boolean available, Long request,
                                      BookingShort lastBooking, BookingShort nextBooking, List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(id);
        itemDto.setName(name);
        itemDto.setDescription(description);
        itemDto.setAvailable(available);
        itemDto.setRequest(request);
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        itemDto.setComments(comments);
        return itemDto;
    }

    public static Booking makeBooking(Long id, LocalDateTime start, LocalDateTime end, Item item, User booker) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(booker);
        booking.setItem(item);
        return booking;
    }

    public static BookingDtoEntry makeBookingDtoEntry(LocalDateTime start, LocalDateTime end, Long itemId) {
        return new BookingDtoEntry(start, end, itemId);
    }

    public static BookingDto makeBookingDto(Long id, LocalDateTime start, LocalDateTime end, ItemDtoShort item, User booker,
                                            BookingStatus status) {
        return new BookingDto(id, start, end, item, booker, status);
    }


}

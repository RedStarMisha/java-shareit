package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingNew;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserDto;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Mapper {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static ItemDto toDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(item.getRequest() == null ? null : item.getRequest().getId());
        return itemDto;
    }

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem().getId(),
                booking.getBooker().getId(), booking.getStatus());
    }

    public static CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        commentDto.setItemId(comment.getItem().getId());
        return commentDto;
    }

    public static BookingNew toResponseEntity(Booking booking) {
        return new BookingNew(booking.getId(), booking.getStart(), booking.getEnd(), booking.getItem(),
                booking.getBooker(), booking.getStatus());
    }

    public static ItemForResponse toResponseEntity(Item item, @Nullable BookingShort last, @Nullable BookingShort next,
                                                   @Nullable List<CommentDto> comments) {
        ItemForResponse itemForResponse = new ItemForResponse();
        itemForResponse.setId(item.getId());
        itemForResponse.setName(item.getName());
        itemForResponse.setDescription(item.getDescription());
        itemForResponse.setAvailable(item.getAvailable());
        itemForResponse.setRequest(item.getRequest() == null ? null : item.getRequest().getId());
        itemForResponse.setLastBooking(last == null ? null : last);
        itemForResponse.setNextBooking(next == null ? null : next);
        itemForResponse.setComments(comments);
        return itemForResponse;
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

    public static Comment toEntity(User author, Item item, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        return comment;
    }

    public static User updateFromDto(User user, UserDto userDto) {
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
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

    public static BookingShort toBookingShort(Booking booking) {
        return booking != null ? new BookingShort(booking.getId(), booking.getBooker().getId()) : null;
    }

}

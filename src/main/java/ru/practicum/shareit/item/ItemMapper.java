package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item toItem(User owner, ItemDtoShort itemDtoShort, @Nullable ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemDtoShort.getName());
        item.setDescription(itemDtoShort.getDescription());
        item.setOwner(owner);
        item.setRequest(itemRequest == null ? null : itemRequest);
        item.setAvailable(itemDtoShort.getAvailable());
        return item;
    }

    public static ItemDtoShort toItemDto(Item item) {
        ItemDtoShort itemDtoShort = new ItemDtoShort();
        itemDtoShort.setId(item.getId());
        itemDtoShort.setName(item.getName());
        itemDtoShort.setDescription(item.getDescription());
        itemDtoShort.setAvailable(item.getAvailable());
        itemDtoShort.setRequestId(item.getRequest() == null ? null : item.getRequest().getId());
        return itemDtoShort;
    }

    public static ItemDto toResponseItem(Item item, @Nullable BookingShort last, @Nullable BookingShort next,
                                         @Nullable List<CommentDto> comments) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequest(item.getRequest() == null ? null : item.getRequest().getId());
        itemDto.setLastBooking(last == null ? null : last);
        itemDto.setNextBooking(next == null ? null : next);
        itemDto.setComments(comments);
        return itemDto;
    }

    public static Item updateFromDto(Item item, ItemDtoShort itemDtoShort) {
        if (itemDtoShort.getName() != null) {
            item.setName(itemDtoShort.getName());
        }
        if (itemDtoShort.getDescription() != null) {
            item.setDescription(itemDtoShort.getDescription());
        }
        if (itemDtoShort.getAvailable() != null) {
            item.setAvailable(itemDtoShort.getAvailable());
        }
        return item;
    }

    public static Comment toComment(User author, Item item, CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        commentDto.setItemId(comment.getItem().getId());
        return commentDto;
    }
}

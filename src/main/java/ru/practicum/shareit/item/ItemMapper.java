package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item toItem(User owner, ItemDtoEntry itemDtoEntry, @Nullable ItemRequest itemRequest) {
        Item item = new Item();
        item.setName(itemDtoEntry.getName());
        item.setDescription(itemDtoEntry.getDescription());
        item.setOwner(owner);
        item.setRequest(itemRequest == null ? null : itemRequest);
        item.setAvailable(itemDtoEntry.getAvailable());
        return item;
    }

    public static ItemDtoEntry toItemDto(Item item) {
        ItemDtoEntry itemDtoEntry = new ItemDtoEntry();
        itemDtoEntry.setId(item.getId());
        itemDtoEntry.setName(item.getName());
        itemDtoEntry.setDescription(item.getDescription());
        itemDtoEntry.setAvailable(item.getAvailable());
        itemDtoEntry.setRequest(item.getRequest() == null ? null : item.getRequest().getId());
        return itemDtoEntry;
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

    public static Item updateFromDto(Item item, ItemDtoEntry itemDtoEntry) {
        if (itemDtoEntry.getName() != null) {
            item.setName(itemDtoEntry.getName());
        }
        if (itemDtoEntry.getDescription() != null) {
            item.setDescription(itemDtoEntry.getDescription());
        }
        if (itemDtoEntry.getAvailable() != null) {
            item.setAvailable(itemDtoEntry.getAvailable());
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

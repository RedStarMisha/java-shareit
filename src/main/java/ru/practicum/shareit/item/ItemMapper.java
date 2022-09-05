package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemForResponse;
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

    public static ItemDtoEntry toCommentDto(Item item) {
        ItemDtoEntry itemDtoEntry = new ItemDtoEntry();
        itemDtoEntry.setId(item.getId());
        itemDtoEntry.setName(item.getName());
        itemDtoEntry.setDescription(item.getDescription());
        itemDtoEntry.setAvailable(item.getAvailable());
        itemDtoEntry.setRequest(item.getRequest() == null ? null : item.getRequest().getId());
        return itemDtoEntry;
    }

    public static ItemForResponse toResponseItem(Item item, @Nullable BookingShort last, @Nullable BookingShort next,
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

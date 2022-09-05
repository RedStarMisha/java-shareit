package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemForResponse;

import java.util.List;

public interface ItemService {

    ItemDtoEntry addItem(long userId, ItemDtoEntry itemDtoEntry);

    ItemForResponse getItemById(long userId, long itemId);

    ItemDtoEntry updateItem(long userId, long itemId, ItemDtoEntry itemDtoEntry);

    List<ItemForResponse> getUserItems(long userId);

    List<ItemDtoEntry> findItemByName(String text);

    CommentDto addComment(long authorId, long itemId, CommentDto commentDto);
}

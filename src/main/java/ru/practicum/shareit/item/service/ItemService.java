package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForResponse;

import java.util.List;

public interface ItemService {

    ItemDto addItem(long userId, ItemDto itemDto);

    ItemForResponse getItemById(long userId, long itemId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    List<ItemForResponse> getUserItems(long userId);

    List<ItemDto> findItemByName(String text);

    CommentDto addComment(long authorId, long itemId, CommentDto commentDto);
}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoEntry;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDtoEntry addItem(long userId, ItemDtoEntry itemDtoEntry);

    ItemDto getItemById(long userId, long itemId);

    ItemDtoEntry updateItem(long userId, long itemId, ItemDtoEntry itemDtoEntry);

    List<ItemDto> getUserItems(long userId);

    List<ItemDtoEntry> findItemByName(String text);

    CommentDto addComment(long authorId, long itemId, CommentDto commentDto);
}

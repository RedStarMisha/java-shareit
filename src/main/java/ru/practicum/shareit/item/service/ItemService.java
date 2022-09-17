package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDtoShort addItem(long userId, ItemDtoShort itemDtoShort);

    ItemDto getItemById(long userId, long itemId);

    ItemDtoShort updateItem(long userId, long itemId, ItemDtoShort itemDtoShort);

    List<ItemDto> getUserItems(long userId, int from, int size);

    List<ItemDtoShort> findItemByName(String text, int from, int size);

    CommentDto addComment(long authorId, long itemId, CommentDto commentDto);
}

package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Item storage into application memory
 */

@Slf4j
@Repository
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> itemStorage = new HashMap<>();
    private final Map<Long, Set<Long>> usersItem = new HashMap<>();
    private long idCounter = 1;

    @Override
    public Item addItem(long userId, ItemDto itemDto) {
        Item item = ItemMapper.convertFromDto(idCounter++, userId, itemDto);
        if (usersItem.containsKey(userId)) {
            usersItem.get(userId).add(item.getId());
        } else {
            usersItem.put(userId, new HashSet<>(Set.of(item.getId())));
        }
        itemStorage.put(item.getId(), item);
        log.info(item + " создана");
        return item;
    }

    @Override
    public Optional<Item> getItemById(long userId, long itemId) {
        if (!itemStorage.containsKey(itemId)) {
            log.warn("item с id = {} не существует", itemId);
            return Optional.empty();
        }
        log.info(itemStorage.get(itemId) + " вызвана");
        return Optional.of(itemStorage.get(itemId));
    }

    @Override
    public Optional<Item> updateItem(long userId, long itemId, ItemDto itemDto) {
        if (!usersItem.containsKey(userId) || !usersItem.get(userId).contains(itemId)) {
            log.warn("у пользователя с id = {} нет item с id = {}", userId, itemId);
            return Optional.empty();
        }
        Item item = itemStorage.get(itemId);
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequest() != null) {
            item.setRequest(itemDto.getRequest());
        }
        log.info(item + " обновлен");
        return Optional.of(item);
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return usersItem.containsKey(userId) ?
                usersItem.get(userId).stream().map(itemStorage::get).collect(Collectors.toList()) : null;
    }

    @Override
    public List<Item> findItemByName(String text) {
        return itemStorage.values().stream().filter(Item::isAvailable)
                .filter(items -> items.getName().toLowerCase().contains(text.toLowerCase()) ||
                        items.getDescription().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
    }

}

package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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

    @Override
    public Item addItem(long userId, Item item) {
        final Set<Long> localItems = usersItem.computeIfAbsent(userId, k -> new HashSet<>());
        localItems.add(item.getId());
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
    public Optional<Item> updateItem(long userId, Item item) {
        if (!usersItem.containsKey(userId) || !usersItem.get(userId).contains(item.getId())) {
            log.warn("у пользователя с id = {} нет item с id = {}", userId, item.getId());
            return Optional.empty();
        }
        final Item itemInStorage = itemStorage.get(item.getId());
        if (item.getName() != null) {
            itemInStorage.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemInStorage.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemInStorage.setAvailable(item.getAvailable());
        }
        if (item.getRequest() != null) {
            itemInStorage.setRequest(item.getRequest());
        }
        log.info(itemInStorage + " обновлен");
        return Optional.of(itemInStorage);
    }

    @Override
    public List<Item> getUserItems(long userId) {
        return usersItem.containsKey(userId) ?
                usersItem.get(userId).stream().map(itemStorage::get).collect(Collectors.toList()) : null;
    }

    @Override
    public List<Item> findItemByName(String text) {
        return itemStorage.values().stream().filter(Item::getAvailable)
                .filter(items -> items.getName().toLowerCase().contains(text.toLowerCase()) ||
                        items.getDescription().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
    }

}

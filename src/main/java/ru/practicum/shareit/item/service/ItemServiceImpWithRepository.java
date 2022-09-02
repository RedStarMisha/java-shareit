package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.notfound.ItemNotFoundException;
import ru.practicum.shareit.exceptions.notfound.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.storage.RequestStorage;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Qualifier("repository")
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpWithRepository implements ItemService {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final RequestStorage requestStorage;


    @Override
    public ItemDto addItem(long userId, ItemDto itemDto) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        ItemRequest itemRequest = requestStorage.getRequest(userId, itemDto.getRequest()).orElse(null);
        Item item = itemRepository.save(Mapper.toEntity(owner, itemDto, itemRequest));
        log.info(item.toString());
        return Mapper.toDto(item);
    }

    @Override
    public ItemDto getItemById(long userId, long itemId) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return itemRepository.findById(itemId).map(Mapper::toDto)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        return itemRepository.findByOwner_IdAndId(userId, itemId).map(item -> {
            Item item1 = Mapper.updateFromDto(item, itemDto);
            itemRepository.save(item1);
            return Mapper.toDto(item1);
        }).orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        return itemRepository.findAllByOwner_Id(userId).stream().map(Mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> findItemByName(String text) {
        return itemRepository.search(text).stream().map(Mapper::toDto).collect(Collectors.toList());
    }
}

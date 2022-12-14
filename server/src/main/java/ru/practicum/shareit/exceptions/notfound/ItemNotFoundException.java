package ru.practicum.shareit.exceptions.notfound;

public class ItemNotFoundException extends EntityNotFoundException {
    public ItemNotFoundException(long itemId) {
        super(String.format("Вещь с id = %d не найдена", itemId));
    }
}

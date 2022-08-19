package ru.practicum.shareit.exceptions;

public class ItemNotFoundException extends EntityNotFoundException{

    private String itemId;

    public ItemNotFoundException(long itemId) {
        super(String.format("Вещь с id = %d не найдена", itemId));
    }
}

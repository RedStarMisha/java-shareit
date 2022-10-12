package ru.practicum.shareit.exceptions;

public class ItemAvailableException extends RuntimeException {

    public ItemAvailableException(long itemId) {
        super("item с id = " + itemId + " не доступна");
    }
}

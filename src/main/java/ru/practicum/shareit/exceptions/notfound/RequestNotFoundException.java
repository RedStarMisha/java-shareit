package ru.practicum.shareit.exceptions.notfound;

public class RequestNotFoundException extends EntityNotFoundException {
    public RequestNotFoundException(long id) {
        super(String.format("Запрос с id = %d не найден", id));

    }
}

package ru.practicum.shareit.exceptions;

public class RequestNotFoundException extends EntityNotFoundException{


    public RequestNotFoundException(long id) {
        super(String.format("Запрос с id = %d не найден", id));

    }
}

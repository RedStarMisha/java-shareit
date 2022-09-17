package ru.practicum.shareit;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exceptions.PaginationParametersException;

public class ShareitUtil {

    public static Pageable makePageParam(int from, int size) {
        if (from < 0 || size < 1) {
            throw new PaginationParametersException("Неверные параметры страницы");
        }
        Sort sort = Sort.by("created").descending();
        return PageRequest.of(from, size, sort);
    }
}

package ru.practicum.shareit.requests.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
}

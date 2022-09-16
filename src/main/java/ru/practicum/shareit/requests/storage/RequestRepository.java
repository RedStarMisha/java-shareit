package ru.practicum.shareit.requests.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    Optional<ItemRequest> findByIdAndAndRequestor_Id(long requestId, long requestorId);

    List<ItemRequest> findAllByRequestor_IdOrderByCreatedDesc(long requestorId);

}

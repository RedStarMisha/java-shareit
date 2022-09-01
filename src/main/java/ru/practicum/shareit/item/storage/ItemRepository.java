package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByOwner_IdAndId(long userId, long itemId);

    List<Item> findAllByOwner_Id(long user_id);

    List<Item> findAllByName(String name);

    @Query("select it from Item as it " +
            "where upper(it.name) like upper(concat('%', ?1,  '%')) " +
            "or upper(it.description) like upper(concat('%', ?1,  '%'))")
    List<Item> findItemByNameAndDesc(String text);
}

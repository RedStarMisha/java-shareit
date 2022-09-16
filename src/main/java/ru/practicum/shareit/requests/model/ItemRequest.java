package ru.practicum.shareit.requests.model;

import lombok.Data;
import ru.practicum.shareit.LocalDateTimeConverter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * // Item Request model .
 */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "description")
    private String description;

    //@Column(name = "requestor")
    @OneToOne(fetch = FetchType.LAZY)
    private User requestor;

    @Column(name = "created")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id") // тут нужно указыва
    private Set<Item> items;
}

package ru.practicum.shareit.requests.model;

import lombok.*;
import ru.practicum.shareit.LocalDateTimeConverter;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * // Item Request model .
 */

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "requests")

public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    private User requestor;

    @Column(name = "created")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "request")
    private Set<Item> items;
}

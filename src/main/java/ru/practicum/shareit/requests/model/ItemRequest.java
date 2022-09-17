package ru.practicum.shareit.requests.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
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
    private long id;

    @Column(name = "description")
    private String description;

    //@Column(name = "requestor")
    @OneToOne(fetch = FetchType.LAZY)
    private User requestor;

    @Column(name = "created")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime created = LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "request")
    //@JoinColumn(name = "request") // тут нужно указыва
    private Set<Item> items;
}

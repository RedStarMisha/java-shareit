package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

/**
 * // Item class in storage .
 */
@Data
@Entity
@Table(name = "items")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(name = "available")
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private ItemRequest request;
}

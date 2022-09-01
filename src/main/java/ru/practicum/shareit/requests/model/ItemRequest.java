package ru.practicum.shareit.requests.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * // Item Request model .
 */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    private long id;

    @Column(name = "description")
    private String description;

    @Column(name = "requestor")
    private long requestor;

    @Column(name = "created")
    private LocalDateTime created;
}

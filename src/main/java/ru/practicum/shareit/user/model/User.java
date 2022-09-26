package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * // User class in storage .
 */
@Data
@Entity
@ToString
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 256)
    private String name;

    @Column(name = "email", length = 512, unique = true)
    private String email;
}

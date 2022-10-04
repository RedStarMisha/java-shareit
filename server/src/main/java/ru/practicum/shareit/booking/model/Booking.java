package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.LocalDateTimeConverter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Data
@Entity
@Table(name = "bookings")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime start;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "end_date")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id")  //указывается имя поля с id в таблице booker, по которой будет связь с user
    private User booker;

    @Enumerated(EnumType.ORDINAL)
    private BookingStatus status = BookingStatus.WAITING;
}

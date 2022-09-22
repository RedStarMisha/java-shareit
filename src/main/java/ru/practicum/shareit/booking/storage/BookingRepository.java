package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select new ru.practicum.shareit.booking.dto.BookingDto(b.id, b.start, b.end, b.item, b.booker, b.status) " +
            "from Booking b" +
            " where (b.item.owner.id=?1 or b.booker.id=?1) AND b.id=?2")
    Optional<BookingDto> findBooking(long userId, long bookingId);

    Optional<Booking> findByIdAndItem_Owner_Id(long bookingId, long ownerId);

    List<BookingDto> findAllByItem_Owner_IdOrderByStartDesc(long userId);

    List<BookingDto> findAllByBooker_IdOrderByStartDesc(long userId);

    // find last and next booking
    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndStartAfterOrderByStartDesc(long itemId, long ownerId,
                                                                                      LocalDateTime start);

    Optional<Booking> findFirstByItem_IdAndItem_Owner_IdAndEndBeforeOrderByEndAsc(long itemId, long ownerId,
                                                                                  LocalDateTime end);

    // for comment
    Optional<Booking> findFirstByItem_IdAndBooker_IdAndEndBefore(long itemId, long bookerId, LocalDateTime date);


}

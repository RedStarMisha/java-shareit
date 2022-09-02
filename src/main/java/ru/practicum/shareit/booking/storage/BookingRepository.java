package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b join Item i on i.id=b.item.id" +
            " where (i.owner.id=?1 or b.booker.id=?1) AND b.id=?2")
    Optional<Booking> findBookingByIdAndUser(long userId, long bookingId);

    Optional<Booking> findByIdAndItem_Owner_Id(long bookingId, long ownerId);

    List<Booking> findAllByItem_Owner_IdOrderByStartAsc(long userId);

    List<Booking> findAllByBooker_IdOrderByStartAsc(long userId);




}

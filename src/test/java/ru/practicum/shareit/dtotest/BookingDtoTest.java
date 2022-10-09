package ru.practicum.shareit.dtotest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestUtil.*;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws IOException {
        var item = makeItemDtoShort(1L, "банан", "желтый", true, null);
        var booker = makeUser(1L, "петя", "asdf@ya.ru");
        var dto = makeBookingDto(
                1L,
                LocalDateTime.of(2022, 9, 23, 15, 22, 30),
                LocalDateTime.of(2022, 9, 24, 15, 22, 30),
                item,
                booker,
                BookingStatus.APPROVED
        );
        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
//        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022.09.23 15:22");
//        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022.09.24 15:22");
        assertThat(result).extractingJsonPathValue("$.item.id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable().booleanValue());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(booker.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(booker.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(booker.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus().toString());

    }
}
package ru.practicum.shareit.dtotest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.TestUtil;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws IOException {
        var last = new BookingShort(1L, 1L);
        var next = new BookingShort(2L, 2L);
        var comment = new CommentDto(1L, "коммент", "вася", 2L,
                LocalDateTime.of(2022, 9, 20, 15, 22, 30));
        var dto = TestUtil.makeItemDto(
                2L,
                "банан",
                "желтый",
                true,
                3L,
                last,
                next,
                List.of(comment));

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.request");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable().booleanValue());
        assertThat(result).extractingJsonPathNumberValue("$.request").isEqualTo(dto.getRequest().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(last.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId")
                .isEqualTo(last.getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(next.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId")
                .isEqualTo(next.getBookerId().intValue());
        assertThat(result).extractingJsonPathArrayValue("$.comments").isInstanceOf(ArrayList.class);
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(comment.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.comments.[0]text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo(comment.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].itemId").isEqualTo(comment.getItemId().intValue());

//        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022.09.24 15:22");
//        assertThat(result).extractingJsonPathValue("$.item.id").isEqualTo(item.getId().intValue());
//        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
//        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(item.getDescription());
//        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.getAvailable().booleanValue());
//        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(booker.getId().intValue());
//        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(booker.getName());
//        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(booker.getEmail());
//        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(dto.getStatus().toString());
    }


}
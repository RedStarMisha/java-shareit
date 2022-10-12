package server.dtotest;

import server.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@ContextConfiguration(classes = {ShareItServer.class})
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
    }


}
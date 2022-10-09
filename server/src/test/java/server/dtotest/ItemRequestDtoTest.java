package server.dtotest;

import server.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.requests.model.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@ContextConfiguration(classes = {ShareItServer.class})
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testSerialize() throws IOException {
        var item = TestUtil.makeItemDtoShort(1L, "банан", "желтый", true, null);
        var dto = TestUtil.makeItemRequestDto(
                1L,
                "желтый банан",
                1L,
                LocalDateTime.of(2022, 9, 23, 15, 22, 30),
                Set.of(item)
        );
        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.requestor");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.requestor").isEqualTo(dto.getRequestor().intValue());
        assertThat(result).extractingJsonPathValue("$.items").isInstanceOf(ArrayList.class);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available")
                .isEqualTo(item.getAvailable().booleanValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isNull();
    }

}
package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = ItemDto.builder()
                .id(1L)
                .name("item1")
                .description("description1")
                .available(true)
                .owner(ItemDto.Owner.builder()
                        .id(1L)
                        .name("user1")
                        .build())
                .comments(new ArrayList<>()).build();

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.owner");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.owner.id").isEqualTo((int) dto.getOwner().getId());
        assertThat(result).extractingJsonPathStringValue("$.owner.name")
                .isEqualTo(dto.getOwner().getName());
        assertThat(result).extractingJsonPathArrayValue("$.comments").isEqualTo(dto.getComments());
    }

    @Test
    void testSerializeWithNull() throws Exception {
        var dto = ItemDto.builder()
                .id(null)
                .name("item1")
                .description("description1")
                .available(true)
                .owner(null)
                .comments(null).build();

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.owner");
        assertThat(result).hasJsonPath("$.comments");
        assertThat(result).doesNotHaveJsonPathValue("$.id");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).doesNotHaveJsonPathValue("$.owner");
        assertThat(result).doesNotHaveJsonPathValue("$.comments");
    }
}

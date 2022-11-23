package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    private JacksonTester<CommentDto> json;
    private final String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss";

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);

    @Test
    void testSerialize() throws Exception {
        var dto = CommentDto.builder().id(1L).text("comment1")
                .itemId(1L)
                .authorName("user1")
                .created(LocalDateTime.of(2022, 11, 1, 1, 1))
                .build();

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(dto.getCreated().format(dateFormatter));
    }

    @Test
    void testSerializeWithNull() throws Exception {
        var dto = CommentDto.builder().id(1L).text("comment1")
                .itemId(null)
                .authorName("user1")
                .created(LocalDateTime.of(2022, 11, 1, 1, 1))
                .build();

        var result = json.write(dto);
        assertThat(result).doesNotHaveJsonPathValue("$.itemId");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(dto.getCreated().format(dateFormatter));

    }
}

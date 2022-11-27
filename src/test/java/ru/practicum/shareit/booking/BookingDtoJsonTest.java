package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDto> json;
    private final String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss";
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateTimePattern);

    @Test
    void testSerialize() throws Exception {
        var dto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2022, 12, 1, 12, 10))
                .end(LocalDateTime.of(2022, 12, 1, 12, 15))
                .item(BookingDto.Item.builder()
                        .id(1L)
                        .name("item1")
                        .build())
                .booker(BookingDto.Booker.builder()
                        .id(1L)
                        .name("user1")
                        .build())
                .status(Status.WAITING)
                .build();

        var result = json.write(dto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(dto.getStart().format(dateFormatter));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(dto.getEnd().format(dateFormatter));
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo((int) dto.getItem().getId());
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo(dto.getItem().getName());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo((int) dto.getBooker().getId());
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo(dto.getBooker().getName());
        assertThat(result).extractingJsonPathStringValue("$.status")
                .isEqualTo(dto.getStatus().toString());
    }

    @Test
    void testSerializeWithNull() throws Exception {
        var dto = BookingDto.builder()
                .id(null)
                .start(LocalDateTime.of(2022, 12, 1, 12, 10))
                .end(LocalDateTime.of(2022, 12, 1, 12, 15))
                .item(null)
                .booker(null)
                .build();

        var result = json.write(dto);
        assertThat(result).doesNotHaveJsonPathValue("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).doesNotHaveJsonPathValue("$.item");
        assertThat(result).doesNotHaveJsonPathValue("$.booker");
        assertThat(result).doesNotHaveJsonPathValue("$.status");
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(dto.getStart().format(dateFormatter));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(dto.getEnd().format(dateFormatter));
    }
}

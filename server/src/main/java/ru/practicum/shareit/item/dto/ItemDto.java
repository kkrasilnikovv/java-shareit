package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.exception.validated_group.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull
    private Boolean available;
    private Owner owner;
    private Booking nextBooking;
    private Booking lastBooking;
    private List<Comment> comments;
    private Long requestId;

    @Data
    @Builder
    public static class Owner {
        private final long id;
        private final String name;
    }

    @Data
    @Builder
    public static class Booking {
        private final Long id;

        private final Long bookerId;
    }

    @Data
    @Builder
    public static class Comment {
        private final Long id;

        private final String text;

        private final String authorName;

        private final LocalDateTime created;
    }
}

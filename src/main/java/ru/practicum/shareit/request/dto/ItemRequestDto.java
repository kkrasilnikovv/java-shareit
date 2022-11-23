package ru.practicum.shareit.request.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Long id;

    @NotBlank
    private String description;

    private User requestor;

    private List<Item> items;

    @NotNull
    private LocalDateTime created;

    @Data
    @Builder
    public static class User {
        private final long id;
        private final String name;
    }

    @Data
    @Builder
    public static class Item {
        private final long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Long requestId;
    }
}

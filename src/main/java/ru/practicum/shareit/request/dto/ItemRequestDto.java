package ru.practicum.shareit.request.dto;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {

    private Integer id;

    @NotBlank
    private String description;

    @NotNull
    private LocalDateTime created;
}

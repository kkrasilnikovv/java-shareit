package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exception.validated_group.Create;
import ru.practicum.shareit.exception.validated_group.Update;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(groups = {Create.class, Update.class})
    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime created;
}

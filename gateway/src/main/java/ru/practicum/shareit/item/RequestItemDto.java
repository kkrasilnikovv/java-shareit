package ru.practicum.shareit.item;



import lombok.Data;
import ru.practicum.shareit.exception.validated_group.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class RequestItemDto {
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long requestId;
}

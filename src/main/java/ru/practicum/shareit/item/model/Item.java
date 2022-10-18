package ru.practicum.shareit.item.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class Item {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private Integer owner;
}

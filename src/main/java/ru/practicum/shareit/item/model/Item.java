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
    private Integer request;


    public Item(Integer id, String name, String description, Boolean available, Integer owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}

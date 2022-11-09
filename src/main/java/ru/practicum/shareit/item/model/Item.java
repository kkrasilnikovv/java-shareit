package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 512)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "last_booking", referencedColumnName = "id")
    private Booking lastBooking;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "next_booking", referencedColumnName = "id")
    private Booking nextBooking;

    @OneToMany
    @JoinTable(name = "item_comments")
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name) && description.equals(item.description) &&
                available.equals(item.available) && owner.equals(item.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, available, owner);
    }
}
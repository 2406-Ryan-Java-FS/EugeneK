package com.eugene.shoegame.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="shoes")
public class ShoeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;
    private Double size;
    private String color;
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY) // where a shoe belongs to one user, and a user can have many shoes.
    @JoinColumn(name = "user_id", nullable = false) // @JoinColumn annotation specifies the foreign key column in the database.
    private UserEntity userEntity;

}

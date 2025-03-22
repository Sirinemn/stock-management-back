package fr.sirine.stock_management_back.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "group")
    private List<User> users;

    @OneToMany(mappedBy = "group")
    private List<Product> products;

    @OneToMany(mappedBy = "group")
    private List<StockMovement> stockMovements;
}


package fr.sirine.stock_management_back.dto;

import fr.sirine.stock_management_back.entities.Product;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Integer id;
    private String name;
    private String description;
    private int quantity;
    private double price;
    private Integer userId;
    private Integer categoryId; // Utilisé pour les requêtes
    private String categoryName; // Utilisé pour l'affichage

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.quantity = product.getQuantity();
        this.price = product.getPrice();
        this.userId = product.getUser().getId();
        this.categoryId = product.getCategory().getId();
        this.categoryName = product.getCategory().getName();
    }
}

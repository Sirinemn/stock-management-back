package fr.sirine.stock_management_back.dto;

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
}

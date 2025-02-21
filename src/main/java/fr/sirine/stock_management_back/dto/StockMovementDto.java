package fr.sirine.stock_management_back.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockMovementDto {
    private Integer id;
    private Integer productId;
    private String type;
    private int quantity;
    private LocalDateTime date;
}

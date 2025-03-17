package fr.sirine.stock_management_back.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DashboardOverviewDto {
    private long totalProducts;
    private long lowStockProducts;
    private List<StockMovementDto> recentMovements;
}

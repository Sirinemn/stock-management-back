package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.DashboardOverviewDto;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.service.IProductService;
import fr.sirine.stock_management_back.service.IStockMovementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final IProductService productService;
    private final IStockMovementService stockMovementService;

    public DashboardService(IProductService productService, IStockMovementService stockMovementService) {
        this.productService = productService;
        this.stockMovementService = stockMovementService;
    }

    public DashboardOverviewDto getDashboardOverview(Integer groupId) {
        long totalProducts = productService.countByGroupId(groupId);
        long lowStockProducts = productService.findAllByGroupId(groupId)
                .stream()
                .filter(product -> product.getQuantity() < product.getThreshold())
                .count();
        List<StockMovementDto> recentMovements = stockMovementService.findTop10ByGroupIdOrderByDateDesc(groupId);

        return new DashboardOverviewDto(totalProducts, lowStockProducts, recentMovements, groupId);
    }
}

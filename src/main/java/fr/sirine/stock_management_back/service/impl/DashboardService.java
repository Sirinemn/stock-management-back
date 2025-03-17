package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.DashboardOverviewDto;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.mapper.StockMovementMapper;
import fr.sirine.stock_management_back.service.IProductService;
import fr.sirine.stock_management_back.service.IStockMovementService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final IProductService productService;
    private final IStockMovementService stockMovementService;

    public DashboardService(IProductService productService, IStockMovementService stockMovementService) {
        this.productService = productService;
        this.stockMovementService = stockMovementService;
    }

    public DashboardOverviewDto getDashboardOverview() {
        long totalProducts = productService.count();
        long lowStockProducts = productService.countByQuantityLessThan(5);
        List<StockMovementDto> recentMovements = stockMovementService.findTop10ByOrderByDateDesc();

        return new DashboardOverviewDto(totalProducts, lowStockProducts, recentMovements);
    }
}

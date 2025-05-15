package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.*;
import fr.sirine.stock_management_back.service.IProductService;
import fr.sirine.stock_management_back.service.IStockMovementService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        // Pour le pie chart : produit -> quantité
        List<ProductQuantityDto> productQuantities = productService.findAllByGroupId(groupId).stream()
                .map(product -> new ProductQuantityDto(product.getName(), product.getQuantity()))
                .collect(Collectors.toList());

        // Pour le line chart : Produit -> Évolution de quantité dans le temps
        List<StockChartSeriesDto> stockChart = generateStockChartData(groupId);

        return DashboardOverviewDto.builder()
                .totalProducts(totalProducts)
                .lowStockProducts(lowStockProducts)
                .recentMovements(recentMovements)
                .productQuantities(productQuantities)
                .stockChart(stockChart)
                .groupId(groupId)
                .build();
    }

    private List<StockChartSeriesDto> generateStockChartData(Integer groupId) {
        // Exemple simple : regroupement des mouvements par produit et date
        List<StockMovementDto> allMovements = stockMovementService.findByGroupId(groupId);

        Map<String, Map<String, Integer>> dataMap = new HashMap<>();

        for (StockMovementDto movement : allMovements) {
            String productName = movement.getProductName();
            String date = movement.getCreatedDate().toLocalDate().toString(); // Format "yyyy-MM-dd"
            int quantity = movement.getQuantity();

            dataMap
                    .computeIfAbsent(productName, k -> new HashMap<>())
                    .merge(date, quantity, Integer::sum);
        }

        List<StockChartSeriesDto> seriesList = new ArrayList<>();

        for (Map.Entry<String, Map<String, Integer>> entry : dataMap.entrySet()) {
            String productName = entry.getKey();
            List<ChartPointDto> points = entry.getValue().entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> new ChartPointDto(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            seriesList.add(new StockChartSeriesDto(productName, points));
        }

        return seriesList;
    }
}


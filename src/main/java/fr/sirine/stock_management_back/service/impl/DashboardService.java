package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.*;
import fr.sirine.stock_management_back.entities.StockMovement;
import fr.sirine.stock_management_back.service.IProductService;
import fr.sirine.stock_management_back.service.IStockMovementService;
import org.springframework.stereotype.Service;

import java.util.*;
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
                .filter(product -> product.getQuantity() <= product.getThreshold())
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
        List<StockMovementDto> allMovements = stockMovementService.findByGroupId(groupId);

        // Map<NomProduit, Map<Date, Quantité>>
        Map<String, Map<String, Integer>> dataMap = new HashMap<>();

        for (StockMovementDto movement : allMovements) {
            String productName = movement.getProductName();
            String date = movement.getCreatedDate().toLocalDate().toString(); // "yyyy-MM-dd"
            int quantity = movement.getQuantity();

            // Ajuster la quantité selon le type de mouvement
            if ("SORTIE".equalsIgnoreCase(movement.getType())) {
                quantity = -quantity;
            }

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

    public List<ProductQuantityDto> getProductQuantities(Integer groupId) {
        return productService.findAllByGroupId(groupId).stream()
                .map(p -> new ProductQuantityDto(p.getName(), p.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<StockChartSeriesDto> getStockChart(Integer groupId) {
        List<StockMovementDto> movements = stockMovementService.findByGroupId(groupId);

        // Regrouper les mouvements par produit
        Map<String, List<StockMovementDto>> groupedByProduct = movements.stream()
                .collect(Collectors.groupingBy(StockMovementDto::getProductName));

        List<StockChartSeriesDto> chartData = new ArrayList<>();

        for (Map.Entry<String, List<StockMovementDto>> entry : groupedByProduct.entrySet()) {
            String productName = entry.getKey();

            // Trier les mouvements par date
            List<ChartPointDto> points = entry.getValue().stream()
                    .sorted(Comparator.comparing(StockMovementDto::getCreatedDate))
                    .map(m -> {
                        int adjustedQuantity = StockMovement.TypeMovement.valueOf(m.getType()).equals(StockMovement.TypeMovement.SORTIE)
                                ? -m.getQuantity()
                                : m.getQuantity();
                        return new ChartPointDto(m.getCreatedDate().toString(), adjustedQuantity);
                    })
                    .collect(Collectors.toList());

            chartData.add(new StockChartSeriesDto(productName, points));
        }

        return chartData;
    }

}


package fr.sirine.stock_management_back.service;

import fr.sirine.stock_management_back.dto.StockMovementDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface IStockMovementService {

    StockMovementDto addStockMovement(StockMovementDto stockMovementDto);
    List<StockMovementDto> getStockMovements(Integer userId, Integer productId, LocalDateTime startDate, LocalDateTime endDate);
    List<StockMovementDto> getStockMovementsByProduct(Integer productId);

    List<StockMovementDto> findTop10ByOrderByDateDesc();
}

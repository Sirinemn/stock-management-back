package fr.sirine.stock_management_back.repository;

import fr.sirine.stock_management_back.entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {
    List<StockMovement> findAllByProductId(Integer productId);
    List<StockMovement> findByUserId(Integer userId);
    List<StockMovement> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<StockMovement> findByUserIdAndProductIdAndDateBetween(Integer userId, Integer productId, LocalDateTime startDate, LocalDateTime endDate);

    List<StockMovement> findTop10ByOrderByDateDesc();
}

package fr.sirine.stock_management_back.repository;

import fr.sirine.stock_management_back.entities.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Integer> {
}

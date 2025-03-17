package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.StockMovement;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.InsufficientStockException;
import fr.sirine.stock_management_back.repository.StockMovementRepository;
import fr.sirine.stock_management_back.service.IProductService;
import fr.sirine.stock_management_back.service.IStockMovementService;
import fr.sirine.stock_management_back.service.IUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMovementService implements IStockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final IProductService productService;
    private final IUserService userService;
    private final StockAlertService stockAlertService;

    public StockMovementService(StockMovementRepository stockMovementRepository, IProductService productService, IUserService userService, StockAlertService stockAlertService) {
        this.stockMovementRepository = stockMovementRepository;
        this.productService = productService;
        this.userService = userService;
        this.stockAlertService = stockAlertService;
    }

    public StockMovementDto addStockMovement(StockMovementDto stockMovementDto) {
        // Récupérer le produit via le service
        Product product = productService.findById(stockMovementDto.getProductId());
        User user = userService.findById(stockMovementDto.getUserId());

        StockMovement stockMovement = new StockMovement();
        stockMovement.setProduct(product);
        stockMovement.setType(StockMovement.TypeMovement.valueOf(stockMovementDto.getType()));
        stockMovement.setQuantity(stockMovementDto.getQuantity());
        stockMovement.setUser(user);
        stockMovement.setDate(LocalDateTime.now());

        // Mise à jour du stock du produit
        if (stockMovement.getType() == StockMovement.TypeMovement.ENTREE) {
            product.setQuantity(product.getQuantity() + stockMovement.getQuantity());
        } else if (stockMovement.getType() == StockMovement.TypeMovement.SORTIE) {
            if (product.getQuantity() < stockMovement.getQuantity()) {
                throw new InsufficientStockException();
            }
            product.setQuantity(product.getQuantity() - stockMovement.getQuantity());
        }

        // Mettre à jour le produit via le service
        productService.updateProduct(new ProductDto(product));
        stockAlertService.checkStockLevel(product);

        stockMovementRepository.save(stockMovement);
        return new StockMovementDto(stockMovement.getId(), stockMovement.getProduct().getId(), stockMovement.getUser().getId(), stockMovement.getType().toString(), stockMovement.getQuantity(), stockMovement.getDate());
    }

    public List<StockMovementDto> getStockMovementsByProduct(Integer productId) {
        List<StockMovement> movements = stockMovementRepository.findAllByProductId((productId));
        return movements.stream()
                .map(m -> new StockMovementDto(m.getId(), m.getProduct().getId(), m.getUser().getId(), m.getType().toString(), m.getQuantity(), m.getDate()))
                .collect(Collectors.toList());
    }
    public List<StockMovementDto> getStockMovements(Integer userId, Integer productId, LocalDateTime startDate, LocalDateTime endDate) {
        List<StockMovement> movements;

        if (userId != null && productId != null && startDate != null && endDate != null) {
            movements = stockMovementRepository.findByUserIdAndProductIdAndDateBetween(userId, productId, startDate, endDate);
        } else if (userId != null) {
            movements = stockMovementRepository.findByUserId(userId);
        } else if (productId != null) {
            movements = stockMovementRepository.findAllByProductId(productId);
        } else if (startDate != null && endDate != null) {
            movements = stockMovementRepository.findByDateBetween(startDate, endDate);
        } else {
            movements = stockMovementRepository.findAll();
        }

        return movements.stream().map(m -> new StockMovementDto(
                m.getId(),
                m.getProduct().getId(),
                m.getUser().getId(),
                m.getType().toString(),
                m.getQuantity(),
                m.getDate())
        ).collect(Collectors.toList());
    }
    public List<StockMovementDto> findTop10ByOrderByDateDesc() {
        List<StockMovement> movements = stockMovementRepository.findTop10ByOrderByDateDesc();
        return movements.stream()
                .map(m -> new StockMovementDto(m.getId(), m.getProduct().getId(), m.getUser().getId(), m.getType().toString(), m.getQuantity(), m.getDate()))
                .collect(Collectors.toList());
    }
}


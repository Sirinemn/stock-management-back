package fr.sirine.stock_management_back.service.impl;

import fr.sirine.stock_management_back.dto.ProductDto;
import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.entities.Group;
import fr.sirine.stock_management_back.entities.Product;
import fr.sirine.stock_management_back.entities.StockMovement;
import fr.sirine.stock_management_back.entities.User;
import fr.sirine.stock_management_back.exceptions.custom.InsufficientStockException;
import fr.sirine.stock_management_back.mapper.StockMovementMapper;
import fr.sirine.stock_management_back.repository.StockMovementRepository;
import fr.sirine.stock_management_back.service.IGroupService;
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
    private final IGroupService groupService;
    private final StockMovementMapper stockMovementMapper;

    public StockMovementService(StockMovementRepository stockMovementRepository, IProductService productService, IUserService userService, StockAlertService stockAlertService, IGroupService groupService, StockMovementMapper stockMovementMapper) {
        this.stockMovementRepository = stockMovementRepository;
        this.productService = productService;
        this.userService = userService;
        this.stockAlertService = stockAlertService;
        this.groupService = groupService;
        this.stockMovementMapper = stockMovementMapper;
    }

    public StockMovementDto addStockMovement(StockMovementDto stockMovementDto) {
        // Récupérer le produit via le service
        Product product = productService.findById(stockMovementDto.getProductId());
        User user = userService.findById(stockMovementDto.getUserId());
        Group group = stockMovementDto.getGroupId() != null ? groupService.findById(stockMovementDto.getGroupId()) : null;

        StockMovement stockMovement = new StockMovement();
        stockMovement.setProduct(product);
        stockMovement.setGroup(group);
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
        return new StockMovementDto(stockMovement.getId(), stockMovement.getProduct().getId(), stockMovement.getUser().getId(), stockMovement.getType().toString(), stockMovement.getQuantity(), stockMovement.getDate(), stockMovement.getGroup().getId());
    }

    @Override
    public List<StockMovementDto> getStockMovementsByProduct(Integer productId, Integer groupId) {
        List<StockMovement> movements;

        if (groupId != null) {
            movements = stockMovementRepository.findByProductIdAndGroupId(productId, groupId);
        } else {
            movements = stockMovementRepository.findAllByProductId(productId);
        }

        return movements.stream()
                .map(m -> new StockMovementDto(m.getId(), m.getProduct().getId(), m.getUser().getId(), m.getType().toString(), m.getQuantity(), m.getDate(), m.getGroup().getId()))
                .collect(Collectors.toList());
    }

    public List<StockMovementDto> getStockMovements(Integer userId, Integer productId, Integer groupId, LocalDateTime startDate, LocalDateTime endDate) {
        List<StockMovement> movements;

        if (userId != null && productId != null && startDate != null && endDate != null) {
            movements = stockMovementRepository.findByUserIdAndProductIdAndGroupIdAndDateBetween(userId, productId, groupId, startDate, endDate);
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
                m.getDate(),
                m.getGroup().getId())
        ).collect(Collectors.toList());
    }
    public List<StockMovementDto> findTop10ByGroupIdOrderByDateDesc(Integer groupId) {
        List<StockMovement> movements = stockMovementRepository.findTop10ByGroupIdOrderByDateDesc(groupId);
        return movements.stream()
                .map(m -> new StockMovementDto(
                        m.getId(),
                        m.getProduct().getId(),
                        m.getUser().getId(),
                        m.getType().toString(),
                        m.getQuantity(),
                        m.getDate(),
                        m.getGroup().getId()))
                .collect(Collectors.toList());
    }
    public List<StockMovementDto> findByGroupId(Integer groupId) {
        List<StockMovement> movements = stockMovementRepository.findByGroupId(groupId);
        return movements.stream()
                .map(stockMovementMapper::toDto)
                .collect(Collectors.toList());
    }
}


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
        return new StockMovementDto(stockMovement.getId(), stockMovement.getProduct().getId(), stockMovement.getProduct().getName(), stockMovement.getUser().getId(), stockMovement.getUser().getFullName(), stockMovement.getType().toString(), stockMovement.getQuantity(), stockMovement.getCreatedDate(),
                stockMovement.getLastModifiedDate(), stockMovement.getGroup().getId());
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
                .map(m -> new StockMovementDto(m.getId(), m.getProduct().getId(), m.getProduct().getName(), m.getUser().getId(), m.getUser().getFullName(), m.getType().toString(), m.getQuantity(),m.getCreatedDate(),
                        m.getLastModifiedDate(), m.getGroup().getId()))
                .collect(Collectors.toList());
    }

    public List<StockMovementDto> getStockMovements(Integer userId, Integer productId, Integer groupId, LocalDateTime startDate, LocalDateTime endDate) {
        List<StockMovement> movements;

        if (userId != null && productId != null && startDate != null && endDate != null) {
            movements = stockMovementRepository.findByUserIdAndProductIdAndGroupIdAndCreatedDateBetween(userId, productId, groupId, startDate, endDate);
        } else if (userId != null) {
            movements = stockMovementRepository.findByUserId(userId);
        } else if (productId != null) {
            movements = stockMovementRepository.findAllByProductId(productId);
        } else if (startDate != null && endDate != null) {
            movements = stockMovementRepository.findByCreatedDateBetween(startDate, endDate);
        } else {
            movements = stockMovementRepository.findAll();
        }

        return movements.stream().map(m -> new StockMovementDto(
                m.getId(),
                m.getProduct().getId(),
                m.getProduct().getName(),
                m.getUser().getId(),
                m.getUser().getFullName(),
                m.getType().toString(),
                m.getQuantity(),
                m.getCreatedDate(),
                m.getLastModifiedDate(),
                m.getGroup().getId())
        ).collect(Collectors.toList());
    }
    public List<StockMovementDto> findTop10ByGroupIdOrderByDateDesc(Integer groupId) {
        List<StockMovement> movements = stockMovementRepository.findTop10ByGroupIdOrderByCreatedDateDesc(groupId);
        return movements.stream()
                .map(m -> new StockMovementDto(
                        m.getId(),
                        m.getProduct().getId(),
                        m.getProduct().getName(),
                        m.getUser().getId(),
                        m.getUser().getFullName(),
                        m.getType().toString(),
                        m.getQuantity(),
                        m.getCreatedDate(),
                        m.getLastModifiedDate(),
                        m.getGroup().getId()))
                .collect(Collectors.toList());
    }
    public List<StockMovementDto> findByGroupId(Integer groupId) {
        List<StockMovement> movements = stockMovementRepository.findByGroupId(groupId);
        return movements.stream()
                .map(stockMovementMapper::toDto)
                .collect(Collectors.toList());
    }
    public void deleteStockMovement(Integer id) {
        StockMovement stockMovement = stockMovementRepository.findById(id).orElseThrow(() -> new RuntimeException("Mouvement de stock non trouvé"));
        StockMovement.TypeMovement type = stockMovement.getType();
        int quantity = stockMovement.getQuantity();
        Product product = stockMovement.getProduct();
        if (type == StockMovement.TypeMovement.ENTREE) {
            product.setQuantity(product.getQuantity() - quantity);
        } else if (type == StockMovement.TypeMovement.SORTIE) {
            product.setQuantity(product.getQuantity() + quantity);
        }
        productService.updateProduct(new ProductDto(product));
        stockMovementRepository.delete(stockMovement);
    }
    public void updateStockMovement(Integer id, StockMovementDto stockMovementDto) {
        StockMovement stockMovement = stockMovementRepository.findById(id).orElseThrow(() -> new RuntimeException("Mouvement de stock non trouvé"));
        // Supprimer le mouvement de stock existant
        StockMovement.TypeMovement type = stockMovement.getType();
        int quantity = stockMovement.getQuantity();
        Product product = stockMovement.getProduct();
        if (type == StockMovement.TypeMovement.ENTREE) {
            product.setQuantity(product.getQuantity() - quantity);
        } else if (type == StockMovement.TypeMovement.SORTIE) {
            product.setQuantity(product.getQuantity() + quantity);
        }
        // Mise à jour du stock du produit
        StockMovement.TypeMovement typeMovement = StockMovement.TypeMovement.valueOf(stockMovementDto.getType());
        if (typeMovement == StockMovement.TypeMovement.ENTREE) {
            product.setQuantity(product.getQuantity() + stockMovementDto.getQuantity());
        } else if (typeMovement == StockMovement.TypeMovement.SORTIE) {
            if (product.getQuantity() < stockMovementDto.getQuantity()) {
                throw new InsufficientStockException();
            }
            product.setQuantity(product.getQuantity() - stockMovementDto.getQuantity());
        }

        // Mettre à jour le produit via le service
        productService.updateProduct(new ProductDto(product));
        stockAlertService.checkStockLevel(product);
        // Mettre à jour le mouvement de stock
        stockMovement.setType(StockMovement.TypeMovement.valueOf(stockMovementDto.getType()));
        stockMovement.setQuantity(stockMovementDto.getQuantity());
        stockMovementRepository.save(stockMovement);
    }
}


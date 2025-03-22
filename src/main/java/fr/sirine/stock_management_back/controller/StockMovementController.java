package fr.sirine.stock_management_back.controller;

import fr.sirine.stock_management_back.dto.StockMovementDto;
import fr.sirine.stock_management_back.payload.response.MessageResponse;
import fr.sirine.stock_management_back.service.impl.StockMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stocks")
@Tag(name = "Stock Movements", description = "API pour la gestion des mouvements de stock")
public class StockMovementController {
    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @Operation(summary = "Ajouter un mouvement de stock", description = "Ajoute une entrée ou une sortie de stock pour un produit donné.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mouvement de stock ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide ou stock insuffisant"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    @PostMapping("/movement")
    public ResponseEntity<MessageResponse> addStockMovement(@RequestBody @Valid StockMovementDto stockMovementDto) {
        StockMovementDto savedMovement = stockMovementService.addStockMovement(stockMovementDto);
        return ResponseEntity.ok(new MessageResponse("Mouvement de stock ajouté avec succès"));
    }
    @Operation(summary = "Lister les mouvements d'un produit", description = "Retourne l'historique des entrées et sorties de stock pour un produit donné.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des mouvements de stock récupérée avec succès"),
            @ApiResponse(responseCode = "404", description = "Produit non trouvé")
    })
    @GetMapping("/movements/{productId}")
    public ResponseEntity<List<StockMovementDto>> getStockMovements(@PathVariable Integer productId, @RequestParam(required = false) Integer groupId) {
        List<StockMovementDto> movements = stockMovementService.getStockMovementsByProduct(productId, groupId);
        return ResponseEntity.ok(movements);
    }
    @Operation(summary = "Obtenir l'historique des mouvements de stock",
            description = "Filtrer par utilisateur, produit et plage de dates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historique récupéré avec succès")
    })
    @GetMapping("/history")
    public ResponseEntity<List<StockMovementDto>> getStockMovements(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer productId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<StockMovementDto> movements = stockMovementService.getStockMovements(userId, productId, groupId, startDate, endDate);
        return ResponseEntity.ok(movements);
    }
}


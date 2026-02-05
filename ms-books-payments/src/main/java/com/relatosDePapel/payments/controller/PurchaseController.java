package com.relatosDePapel.payments.controller;

import com.relatosDePapel.payments.dto.PurchaseCreateRequestDTO;
import com.relatosDePapel.payments.dto.PurchaseResponseDTO;
import com.relatosDePapel.payments.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments/purchases")
@Tag(name = "Purchases", description = "API de gestión de compras de libros")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * POST /payments/purchases - Registrar una nueva compra
     * Valida libros consultando a ms-books-catalogue vía Eureka
     */
    @Operation(summary = "Registrar una compra",
               description = "Crea una nueva compra validando existencia, visibilidad y stock de los libros en ms-books-catalogue")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compra creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado"),
        @ApiResponse(responseCode = "409", description = "Libro no visible o sin stock suficiente")
    })
    @PostMapping
    public ResponseEntity<PurchaseResponseDTO> createPurchase(
            @Valid @RequestBody PurchaseCreateRequestDTO dto) {
        PurchaseResponseDTO response = purchaseService.createPurchase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /payments/purchases - Listar todas las compras
     */
    @Operation(summary = "Listar todas las compras",
               description = "Obtiene la lista de todas las compras registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de compras obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<PurchaseResponseDTO>> getAllPurchases() {
        List<PurchaseResponseDTO> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    /**
     * GET /payments/purchases/{id} - Obtener una compra por ID
     */
    @Operation(summary = "Obtener compra por ID",
               description = "Obtiene los detalles de una compra específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra encontrada"),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDTO> getPurchaseById(
            @Parameter(description = "ID de la compra", required = true) @PathVariable Long id) {
        PurchaseResponseDTO purchase = purchaseService.getPurchaseById(id);
        return ResponseEntity.ok(purchase);
    }
}

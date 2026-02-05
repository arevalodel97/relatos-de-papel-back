package com.relatosDePapel.payments.service;

import com.relatosDePapel.payments.dto.PurchaseCreateRequestDTO;
import com.relatosDePapel.payments.dto.PurchaseResponseDTO;

import java.util.List;

/**
 * Servicio para la gestión de compras
 */
public interface PurchaseService {

    /**
     * Registrar una nueva compra
     * Valida existencia, visibilidad y stock consultando a ms-books-catalogue
     */
    PurchaseResponseDTO createPurchase(PurchaseCreateRequestDTO dto);

    /**
     * Listar todas las compras
     */
    List<PurchaseResponseDTO> getAllPurchases();

    /**
     * Obtener una compra por ID
     */
    PurchaseResponseDTO getPurchaseById(Long id);
}

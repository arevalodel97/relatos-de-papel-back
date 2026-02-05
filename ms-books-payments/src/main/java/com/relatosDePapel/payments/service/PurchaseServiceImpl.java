package com.relatosDePapel.payments.service;

import com.relatosDePapel.payments.client.CatalogueClient;
import com.relatosDePapel.payments.dto.BookDTO;
import com.relatosDePapel.payments.dto.PurchaseCreateRequestDTO;
import com.relatosDePapel.payments.dto.PurchaseItemDTO;
import com.relatosDePapel.payments.dto.PurchaseResponseDTO;
import com.relatosDePapel.payments.entity.Customer;
import com.relatosDePapel.payments.entity.Purchase;
import com.relatosDePapel.payments.entity.PurchaseItem;
import com.relatosDePapel.payments.exception.BookNotVisibleException;
import com.relatosDePapel.payments.exception.InsufficientStockException;
import com.relatosDePapel.payments.exception.PurchaseNotFoundException;
import com.relatosDePapel.payments.mapper.PurchaseMapper;
import com.relatosDePapel.payments.repository.PurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de compras
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final CatalogueClient catalogueClient;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                              PurchaseMapper purchaseMapper,
                              CatalogueClient catalogueClient) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseMapper = purchaseMapper;
        this.catalogueClient = catalogueClient;
    }

    /**
     * Registrar una nueva compra
     * Flujo:
     * 1. Validar cada libro (existencia, visible=true, stock suficiente)
     * 2. Crear la compra
     * 3. Persistir en MySQL
     */
    @Override
    @Transactional
    public PurchaseResponseDTO createPurchase(PurchaseCreateRequestDTO dto) {
        // Validar todos los libros antes de crear la compra
        for (PurchaseItemDTO itemDTO : dto.getItems()) {
            validateBook(itemDTO.getBookId(), itemDTO.getQuantity());
        }

        // Crear la entidad Purchase
        Customer customer = purchaseMapper.toCustomer(dto.getCustomer());
        Purchase purchase = new Purchase(customer);

        // Agregar items
        for (PurchaseItemDTO itemDTO : dto.getItems()) {
            PurchaseItem item = new PurchaseItem(itemDTO.getBookId(), itemDTO.getQuantity());
            purchase.addItem(item);
        }

        // Persistir
        Purchase savedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toResponseDTO(savedPurchase);
    }

    /**
     * Valida un libro consultando a ms-books-catalogue
     * Lanza excepciones si:
     * - El libro no existe (404)
     * - El libro no está visible (409)
     * - No hay stock suficiente (409)
     */
    private void validateBook(Long bookId, Integer quantity) {
        // Llamada HTTP a ms-books-catalogue vía Eureka
        BookDTO book = catalogueClient.getBookById(bookId);

        // Validar que el libro esté visible
        if (book.getVisible() == null || !book.getVisible()) {
            throw new BookNotVisibleException(bookId);
        }

        // Validar stock suficiente
        if (book.getStock() == null || book.getStock() < quantity) {
            throw new InsufficientStockException(bookId, quantity, book.getStock());
        }
    }

    /**
     * Listar todas las compras
     */
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseResponseDTO> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener una compra por ID
     */
    @Override
    @Transactional(readOnly = true)
    public PurchaseResponseDTO getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new PurchaseNotFoundException(id));
        return purchaseMapper.toResponseDTO(purchase);
    }
}

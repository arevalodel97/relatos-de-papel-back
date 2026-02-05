package com.relatosDePapel.payments.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class PurchaseCreateRequestDTO {

    @NotNull(message = "Los datos del cliente son obligatorios")
    @Valid
    private CustomerDTO customer;

    @NotEmpty(message = "La compra debe incluir al menos un libro")
    @Valid
    private List<PurchaseItemDTO> items;

    public PurchaseCreateRequestDTO() {
    }

    public PurchaseCreateRequestDTO(CustomerDTO customer, List<PurchaseItemDTO> items) {
        this.customer = customer;
        this.items = items;
    }

    // Getters and Setters
    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public List<PurchaseItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemDTO> items) {
        this.items = items;
    }
}

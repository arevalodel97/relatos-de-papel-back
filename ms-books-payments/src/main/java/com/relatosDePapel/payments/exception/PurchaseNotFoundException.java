package com.relatosDePapel.payments.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(Long id) {
        super("Compra no encontrada con ID: " + id);
    }
}

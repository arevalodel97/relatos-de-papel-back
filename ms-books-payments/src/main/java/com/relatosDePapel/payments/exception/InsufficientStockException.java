package com.relatosDePapel.payments.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long bookId, Integer requested, Integer available) {
        super(String.format("Stock insuficiente para el libro ID %d. Solicitado: %d, Disponible: %d",
                            bookId, requested, available));
    }
}

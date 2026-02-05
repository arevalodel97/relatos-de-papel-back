package com.relatosDePapel.payments.exception;

public class BookNotVisibleException extends RuntimeException {
    public BookNotVisibleException(Long id) {
        super("El libro con ID " + id + " no está disponible para la venta (no visible)");
    }
}

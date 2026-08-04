package com.mycompany.javabeans_cafe.enums;

public enum EstadoPago {
    PENDIENTE("PENDIENTE"),
    PAGADO("PAGADO");

    private final String estadoPago;

    EstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }
}

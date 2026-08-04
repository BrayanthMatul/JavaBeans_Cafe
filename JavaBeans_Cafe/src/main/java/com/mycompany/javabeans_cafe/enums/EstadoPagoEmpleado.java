package com.mycompany.javabeans_cafe.enums;

public enum EstadoPagoEmpleado {
    PENDIENTE("PENDIENTE"),
    PAGADO("PAGADO");

    private final String estadoPago;

    EstadoPagoEmpleado(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }
}

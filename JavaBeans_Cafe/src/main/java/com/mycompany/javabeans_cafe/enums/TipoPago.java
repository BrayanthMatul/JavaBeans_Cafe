package com.mycompany.javabeans_cafe.enums;

public enum TipoPago {
    QUINCENA("QUINCENA"),
    FIN_DE_MES("MENSUAL");

    private final String tipoPago;

    TipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }
}

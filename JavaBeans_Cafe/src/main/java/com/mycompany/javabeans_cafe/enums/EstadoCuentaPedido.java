package com.mycompany.javabeans_cafe.enums;

public enum EstadoCuentaPedido {
    ABIERTA("ABIERTA"),
    PAGADA("PAGADA");

    private final String estado;

    EstadoCuentaPedido(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

}

package com.mycompany.javabeans_cafe.enums;

public enum EstadoMesa {
    LIBRE("LIBRE"),
    OCUPADA("OCUPADA");

    private final String estado;

    EstadoMesa(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}

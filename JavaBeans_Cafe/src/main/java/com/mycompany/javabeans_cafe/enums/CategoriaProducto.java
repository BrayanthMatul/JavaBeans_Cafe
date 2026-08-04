package com.mycompany.javabeans_cafe.enums;

public enum CategoriaProducto {
    BEBIDA_CALIENTE("BEBIDA_CALIENTE"),
    BEBIDA_FRIA("BEBIDA_FRIA"),
    POSTRES("POSTRE"),
    COMIDA("COMIDA");

    private final String categoria;

    CategoriaProducto(String categoria) {
        this.categoria = categoria;
    }

    public String getCategoria() {
        return categoria;
    }
}

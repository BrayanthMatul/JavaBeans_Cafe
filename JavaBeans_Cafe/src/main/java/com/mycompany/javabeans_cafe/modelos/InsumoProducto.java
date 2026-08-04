package com.mycompany.javabeans_cafe.modelos;

public class InsumoProducto {
    private int id;
    private int codigoInsumo;
    private int codigoProducto;
    private int cantidad;

    public InsumoProducto(int id, int codigoInsumo, int codigoProducto, int cantidad) {
        this.id = id;
        this.codigoInsumo = codigoInsumo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
    }

    public InsumoProducto(int codigoInsumo, int codigoProducto, int cantidad) {
        this.codigoInsumo = codigoInsumo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
    }
}

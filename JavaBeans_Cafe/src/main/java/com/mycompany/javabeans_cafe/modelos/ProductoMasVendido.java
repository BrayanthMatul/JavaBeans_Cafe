package com.mycompany.javabeans_cafe.modelos;

public class ProductoMasVendido {

    private int codigoProducto;
    private String nombreProducto;
    private int cantidadVendida;

    public ProductoMasVendido(
            int codigoProducto,
            String nombreProducto,
            int cantidadVendida) {

        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }
}
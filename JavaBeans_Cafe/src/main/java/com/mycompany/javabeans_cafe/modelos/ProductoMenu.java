package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;

import com.mycompany.javabeans_cafe.enums.CategoriaProducto;

public class ProductoMenu {
    private int codigoProducto;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private BigDecimal precioVenta;
    private byte[] imagen;

    public ProductoMenu(int codigoProducto, String nombreProducto, CategoriaProducto categoria, BigDecimal precioVenta,
            byte[] imagen) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
    }

    public ProductoMenu(String nombreProducto, CategoriaProducto categoria, BigDecimal precioVenta, byte[] imagen) {
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
    }

}

package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;

import com.mycompany.javabeans_cafe.enums.CategoriaProducto;

public class ProductoMenu {
    private int codigoProducto;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private BigDecimal precioVenta;
    private byte[] imagen;
    private boolean disponible; // Hay que borrarlo en el Sql y aqui, se hara la validacion al momento de realizar el pedido

    public ProductoMenu(int codigoProducto, String nombreProducto, CategoriaProducto categoria, BigDecimal precioVenta,
            byte[] imagen, boolean disponible) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
        this.disponible = disponible;
    }

    public ProductoMenu(String nombreProducto, CategoriaProducto categoria, BigDecimal precioVenta, byte[] imagen,
            boolean disponible) {
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
        this.disponible = disponible;
    }

}

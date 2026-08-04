package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;

public class ProductoPedido {
    private int id;
    private int codigoProducto;
    private int codigoPedido;
    private int cantidad;
    private BigDecimal subtotal;

    public ProductoPedido(int id, int codigoProducto, int codigoPedido, int cantidad, BigDecimal subtotal) {
        this.id = id;
        this.codigoProducto = codigoProducto;
        this.codigoPedido = codigoPedido;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public ProductoPedido(int codigoProducto, int codigoPedido, int cantidad, BigDecimal subtotal) {
        this.codigoProducto = codigoProducto;
        this.codigoPedido = codigoPedido;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

}

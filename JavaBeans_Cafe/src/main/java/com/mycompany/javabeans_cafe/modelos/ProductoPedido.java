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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public int getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(int codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

}

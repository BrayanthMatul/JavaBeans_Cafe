package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;

public class InsumoProducto {
    private int id;
    private int codigoInsumo;
    private int codigoProducto;
    private BigDecimal cantidad;

    public InsumoProducto(int id, int codigoInsumo, int codigoProducto, BigDecimal cantidad) {
        this.id = id;
        this.codigoInsumo = codigoInsumo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
    }

    public InsumoProducto(int codigoInsumo, int codigoProducto, BigDecimal cantidad) {
        this.codigoInsumo = codigoInsumo;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(int codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

}

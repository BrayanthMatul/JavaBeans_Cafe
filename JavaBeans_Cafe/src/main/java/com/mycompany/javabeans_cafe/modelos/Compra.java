package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Compra {

    private int codigoCompra;
    private int codigoInsumo;
    private Timestamp fechaCompra;
    private int cantidad;
    private BigDecimal monto;
    private boolean contabilizado;

    public Compra(int codigoCompra, int codigoInsumo, Timestamp fechaCompra, int cantidad, BigDecimal monto,
            boolean contabilizado) {
        this.codigoCompra = codigoCompra;
        this.codigoInsumo = codigoInsumo;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.monto = monto;
        this.contabilizado = contabilizado;
    }

    public Compra(int codigoInsumo, Timestamp fechaCompra, int cantidad, BigDecimal monto, boolean contabilizado) {
        this.codigoInsumo = codigoInsumo;
        this.fechaCompra = fechaCompra;
        this.cantidad = cantidad;
        this.monto = monto;
        this.contabilizado = contabilizado;
    }

    public int getCodigoCompra() {
        return codigoCompra;
    }

    public void setCodigoCompra(int codigoCompra) {
        this.codigoCompra = codigoCompra;
    }

    public int getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(int codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public Timestamp getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Timestamp fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public boolean isContabilizado() {
        return contabilizado;
    }

    public void setContabilizado(boolean contabilizado) {
        this.contabilizado = contabilizado;
    }
    
    
}

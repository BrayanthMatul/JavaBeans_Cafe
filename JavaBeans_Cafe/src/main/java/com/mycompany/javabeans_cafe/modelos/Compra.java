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
}

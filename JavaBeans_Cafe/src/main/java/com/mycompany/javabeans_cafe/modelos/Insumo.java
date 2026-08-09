package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;

public class Insumo {
    private int codigoInsumo;
    private String nombreInsumo;
    private String unidadMedida;
    private int stockActual;
    private int stockMinimo;
    private BigDecimal costoInsumo;

    public Insumo(int codigoInsumo, String nombreInsumo, String unidadMedida, int stockActual, int stockMinimo,
            BigDecimal costoInsumo) {
        this.codigoInsumo = codigoInsumo;
        this.nombreInsumo = nombreInsumo;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.costoInsumo = costoInsumo;
    }

    public Insumo(String nombreInsumo, String unidadMedida, int stockActual, int stockMinimo,
            BigDecimal costoInsumo) {
        this.nombreInsumo = nombreInsumo;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.costoInsumo = costoInsumo;
    }

    public int getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(int codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public BigDecimal getCostoInsumo() {
        return costoInsumo;
    }

    public void setCostoInsumo(BigDecimal costoInsumo) {
        this.costoInsumo = costoInsumo;
    }
    
    
}

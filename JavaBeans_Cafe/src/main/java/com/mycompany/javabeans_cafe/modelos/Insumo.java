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
}

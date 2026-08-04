package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.mycompany.javabeans_cafe.enums.EstadoPagoEmpleado;
import com.mycompany.javabeans_cafe.enums.TipoPago;

public class PagoSalario {
    private int codigoNomina;
    private int codigoEmpleado;
    private String dpiEmpleado;
    private Timestamp fechaHoraEmision;
    private TipoPago tipoPago;
    private BigDecimal montoPago;
    private EstadoPagoEmpleado estado;

    // Faltan correcciones en el sql
    // Aniadir el dpi
    // El estado es Varcachar

    public PagoSalario(int codigoNomina, int codigoEmpleado, String dpiEmpleado, Timestamp fechaHoraEmision,
            TipoPago tipoPago, BigDecimal montoPago, EstadoPagoEmpleado estado) {
        this.codigoNomina = codigoNomina;
        this.codigoEmpleado = codigoEmpleado;
        this.dpiEmpleado = dpiEmpleado;
        this.fechaHoraEmision = fechaHoraEmision;
        this.tipoPago = tipoPago;
        this.montoPago = montoPago;
        this.estado = estado;
    }

    public PagoSalario(int codigoEmpleado, String dpiEmpleado, Timestamp fechaHoraEmision, TipoPago tipoPago,
            BigDecimal montoPago, EstadoPagoEmpleado estado) {
        this.codigoEmpleado = codigoEmpleado;
        this.dpiEmpleado = dpiEmpleado;
        this.fechaHoraEmision = fechaHoraEmision;
        this.tipoPago = tipoPago;
        this.montoPago = montoPago;
        this.estado = estado;
    }

}

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

    public int getCodigoNomina() {
        return codigoNomina;
    }

    public void setCodigoNomina(int codigoNomina) {
        this.codigoNomina = codigoNomina;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getDpiEmpleado() {
        return dpiEmpleado;
    }

    public void setDpiEmpleado(String dpiEmpleado) {
        this.dpiEmpleado = dpiEmpleado;
    }

    public Timestamp getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public void setFechaHoraEmision(Timestamp fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(BigDecimal montoPago) {
        this.montoPago = montoPago;
    }

    public EstadoPagoEmpleado getEstado() {
        return estado;
    }

    public void setEstado(EstadoPagoEmpleado estado) {
        this.estado = estado;
    }
    

}

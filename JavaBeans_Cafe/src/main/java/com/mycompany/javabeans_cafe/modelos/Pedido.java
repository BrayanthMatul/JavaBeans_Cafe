package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.mycompany.javabeans_cafe.enums.EstadoCuentaPedido;

public class Pedido {
    private int codigoPedido;
    private int codigoEmpleado;
    private int numeroMesa;
    private Timestamp fechaHoraOcupacion;
    private Timestamp fechaHoraLiberacion;
    private BigDecimal propina;
    private BigDecimal montoPedido;
    private EstadoCuentaPedido estadoCuenta;
    private boolean contabilizado;

    public Pedido(int codigoPedido, int codigoEmpleado, int numeroMesa, Timestamp fechaHoraOcupacion,
            Timestamp fechaHoraLiberacion, BigDecimal propina, BigDecimal montoPedido, EstadoCuentaPedido estadoCuenta,
            boolean contabilizado) {
        this.codigoPedido = codigoPedido;
        this.codigoEmpleado = codigoEmpleado;
        this.numeroMesa = numeroMesa;
        this.fechaHoraOcupacion = fechaHoraOcupacion;
        this.fechaHoraLiberacion = fechaHoraLiberacion;
        this.propina = propina;
        this.montoPedido = montoPedido;
        this.estadoCuenta = estadoCuenta;
        this.contabilizado = contabilizado;
    }

    public Pedido(int codigoEmpleado, int numeroMesa, Timestamp fechaHoraOcupacion, Timestamp fechaHoraLiberacion,
            BigDecimal propina, BigDecimal montoPedido, EstadoCuentaPedido estadoCuenta, boolean contabilizado) {
        this.codigoEmpleado = codigoEmpleado;
        this.numeroMesa = numeroMesa;
        this.fechaHoraOcupacion = fechaHoraOcupacion;
        this.fechaHoraLiberacion = fechaHoraLiberacion;
        this.propina = propina;
        this.montoPedido = montoPedido;
        this.estadoCuenta = estadoCuenta;
        this.contabilizado = contabilizado;
    }

    public int getCodigoPedido() {
        return codigoPedido;
    }

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public Timestamp getFechaHoraOcupacion() {
        return fechaHoraOcupacion;
    }

    public Timestamp getFechaHoraLiberacion() {
        return fechaHoraLiberacion;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public BigDecimal getMontoPedido() {
        return montoPedido;
    }

    public EstadoCuentaPedido getEstadoCuenta() {
        return estadoCuenta;
    }

    public boolean isContabilizado() {
        return contabilizado;
    }

    public void setCodigoPedido(int codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public void setFechaHoraOcupacion(Timestamp fechaHoraOcupacion) {
        this.fechaHoraOcupacion = fechaHoraOcupacion;
    }

    public void setFechaHoraLiberacion(Timestamp fechaHoraLiberacion) {
        this.fechaHoraLiberacion = fechaHoraLiberacion;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public void setMontoPedido(BigDecimal montoPedido) {
        this.montoPedido = montoPedido;
    }

    public void setEstadoCuenta(EstadoCuentaPedido estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public void setContabilizado(boolean contabilizado) {
        this.contabilizado = contabilizado;
    }

}

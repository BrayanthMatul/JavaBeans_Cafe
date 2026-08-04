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
}

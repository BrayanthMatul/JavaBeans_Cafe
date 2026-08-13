package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class BalanceFinanciero {
    private int id;
    private Timestamp fecha_hora;
    private BigDecimal monto_ingresos;
    private BigDecimal monto_egresos;
    private BigDecimal balance;

    // Contructor para recuperar Balance
    public BalanceFinanciero(int id, Timestamp fecha_hora, BigDecimal monto_ingresos, BigDecimal monto_egresos,
            BigDecimal balance) {
        this.id = id;
        this.fecha_hora = fecha_hora;
        this.monto_ingresos = monto_ingresos;
        this.monto_egresos = monto_egresos;
        this.balance = balance;
    }

    // Constructor para crear un nuevo Balance
    public BalanceFinanciero(BigDecimal monto_ingresos, BigDecimal monto_egresos, BigDecimal balance) {
        this.monto_ingresos = monto_ingresos;
        this.monto_egresos = monto_egresos;
        this.balance = balance;
        this.fecha_hora = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public Timestamp getFechaHora() {
        return fecha_hora;
    }

    public BigDecimal getMontoIngresos() {
        return monto_ingresos;
    }

    public BigDecimal getMontoEgresos() {
        return monto_egresos;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFechaHora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public void setMontoIngresos(BigDecimal monto_ingresos) {
        this.monto_ingresos = monto_ingresos;
    }

    public void setMontoEgresos(BigDecimal monto_egresos) {
        this.monto_egresos = monto_egresos;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}

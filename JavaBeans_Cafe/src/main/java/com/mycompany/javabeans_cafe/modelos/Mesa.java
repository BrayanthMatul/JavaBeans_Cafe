package com.mycompany.javabeans_cafe.modelos;

import com.mycompany.javabeans_cafe.enums.EstadoMesa;

public class Mesa {
    private int numeroMesa;
    private int capacidad;
    private EstadoMesa estado;

    public Mesa(int numeroMesa, int capacidad, EstadoMesa estado) {
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public Mesa(int capacidad) {
        this.capacidad = capacidad;
        this.estado = EstadoMesa.LIBRE;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoMesa getEstado() {
        return estado;
    }

    public void setEstado(EstadoMesa estado) {
        this.estado = estado;
    }

}

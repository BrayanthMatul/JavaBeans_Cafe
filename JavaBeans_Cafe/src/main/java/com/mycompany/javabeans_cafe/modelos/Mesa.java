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

    public Mesa(int numeroMesa, int capacidad) {
        this.numeroMesa = numeroMesa;
        this.capacidad = capacidad;
        this.estado = EstadoMesa.LIBRE;
    }
}

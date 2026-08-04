package com.mycompany.javabeans_cafe.enums;

public enum EmpleadoRol {
    ADMINISTRADOR("ADMINISTRADOR"),
    MESERO("MESERO"),
    BARISTA("BARISTA"),
    COCINA("COCINA");

    private final String tipoEmpleado;

    EmpleadoRol(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }
}

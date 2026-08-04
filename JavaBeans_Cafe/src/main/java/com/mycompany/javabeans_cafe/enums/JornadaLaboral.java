package com.mycompany.javabeans_cafe.enums;

public enum JornadaLaboral {
    MATUTINA("MATUTINA"),
    VESPERTINA("VESPERTINA"),
    NOCTURNA("NOCTURNA");

    private final String jornada;

    JornadaLaboral(String jornada) {
        this.jornada = jornada;
    }

    public String getJornada() {
        return jornada;
    }
}

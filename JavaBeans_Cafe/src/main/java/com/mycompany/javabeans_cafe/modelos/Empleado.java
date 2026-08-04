package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.mycompany.javabeans_cafe.enums.EmpleadoRol;
import com.mycompany.javabeans_cafe.enums.JornadaLaboral;

public class Empleado {
    private int codigoEmpleado;
    private String dpi;
    private String nombreCompleto;
    private String nombreUsuario;
    private String contrasena;
    private EmpleadoRol rol;
    private JornadaLaboral jornadaLaboral;
    private BigDecimal salario;
    private LocalDate fechaContratacion;
    private boolean activo;

    public Empleado(int codigoEmpleado, String dpi, String nombreCompleto, String nombreUsuario, String contrasena,
            EmpleadoRol rol, JornadaLaboral jornadaLaboral, BigDecimal salario, LocalDate fechaContratacion,
            boolean activo) {
        this.codigoEmpleado = codigoEmpleado;
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.jornadaLaboral = jornadaLaboral;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }

    public Empleado(String dpi, String nombreCompleto, String nombreUsuario, String contrasena, EmpleadoRol rol,
            JornadaLaboral jornadaLaboral, BigDecimal salario, LocalDate fechaContratacion, boolean activo) {
        this.dpi = dpi;
        this.nombreCompleto = nombreCompleto;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.jornadaLaboral = jornadaLaboral;
        this.salario = salario;
        this.fechaContratacion = fechaContratacion;
        this.activo = activo;
    }
}

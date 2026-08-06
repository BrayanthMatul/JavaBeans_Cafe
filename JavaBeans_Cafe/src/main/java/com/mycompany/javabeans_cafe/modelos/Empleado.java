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

    public int getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public String getDpi() {
        return dpi;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public EmpleadoRol getRol() {
        return rol;
    }

    public JornadaLaboral getJornadaLaboral() {
        return jornadaLaboral;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public boolean isActivo() {
        return activo;
    }

    // SEtters

    public void setCodigoEmpleado(int codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(EmpleadoRol rol) {
        this.rol = rol;
    }

    public void setJornadaLaboral(JornadaLaboral jornadaLaboral) {
        this.jornadaLaboral = jornadaLaboral;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
        this.fechaContratacion = fechaContratacion;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

}

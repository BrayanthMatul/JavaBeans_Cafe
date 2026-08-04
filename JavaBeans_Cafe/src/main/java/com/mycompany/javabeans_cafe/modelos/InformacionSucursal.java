package com.mycompany.javabeans_cafe.modelos;

public class InformacionSucursal {
    private int idSucursal;
    private String nombreSucursal;
    private String telefono;
    private String direccion;
    private boolean completado;

    public InformacionSucursal(int idSucursal, String nombreSucursal, String telefono, String direccion,
            boolean completado) {
        this.idSucursal = idSucursal;
        this.nombreSucursal = nombreSucursal;
        this.telefono = telefono;
        this.direccion = direccion;
        this.completado = completado;
    }

    public InformacionSucursal(String nombreSucursal, String telefono, String direccion, boolean completado) {
        this.nombreSucursal = nombreSucursal;
        this.telefono = telefono;
        this.direccion = direccion;
        this.completado = completado;
    }
}

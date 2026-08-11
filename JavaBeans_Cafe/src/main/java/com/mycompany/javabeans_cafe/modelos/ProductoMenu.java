package com.mycompany.javabeans_cafe.modelos;

import java.math.BigDecimal;

import com.mycompany.javabeans_cafe.enums.CategoriaProducto;

public class ProductoMenu {
    private int codigoProducto;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private BigDecimal precioVenta;
    private byte[] imagen;

    public ProductoMenu(int codigoProducto, String nombreProducto, CategoriaProducto categoria, BigDecimal precioVenta,
            byte[] imagen) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
    }

    public ProductoMenu(String nombreProducto, CategoriaProducto categoria, BigDecimal precioVenta, byte[] imagen) {
        this.nombreProducto = nombreProducto;
        this.categoria = categoria;
        this.precioVenta = precioVenta;
        this.imagen = imagen;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public CategoriaProducto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    
    

}

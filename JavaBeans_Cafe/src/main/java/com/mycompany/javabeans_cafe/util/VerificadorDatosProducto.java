package com.mycompany.javabeans_cafe.util;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.mycompany.javabeans_cafe.daos.ProductoMenuDAO;

public class VerificadorDatosProducto {

    private final ProductoMenuDAO productoDAO;

    public VerificadorDatosProducto() {
        productoDAO = new ProductoMenuDAO();
    }

    public boolean existeNombreProducto(String nombreProducto) throws SQLException {
        return productoDAO.encontrarPorNombre(nombreProducto) != null;
    }

    public boolean bigDecimalMayorQueCero(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }

}

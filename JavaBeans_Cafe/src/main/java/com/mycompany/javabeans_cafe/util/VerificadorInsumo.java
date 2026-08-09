package com.mycompany.javabeans_cafe.util;

import java.sql.SQLException;

import com.mycompany.javabeans_cafe.daos.InsumoDAO;

public class VerificadorInsumo {

    private final InsumoDAO insumoDAO;

    public VerificadorInsumo() {
        this.insumoDAO = new InsumoDAO();
    }

    public boolean existeNombreInsumo(String nombreInsumo)
            throws SQLException {

        return insumoDAO.obtenerPorNombre(nombreInsumo) != null;
    }

}

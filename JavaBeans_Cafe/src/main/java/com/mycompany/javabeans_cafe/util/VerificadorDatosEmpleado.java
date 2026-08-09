package com.mycompany.javabeans_cafe.util;

import java.sql.SQLException;

import com.mycompany.javabeans_cafe.daos.EmpleadoDAO;

public class VerificadorDatosEmpleado {

    private final EmpleadoDAO empleadoDAO;

    public VerificadorDatosEmpleado() {
        empleadoDAO = new EmpleadoDAO();
    }

    public boolean existeNombreUsuario(String nombreUsuario)
            throws SQLException {

        return empleadoDAO.encontrarPorNombreUsuario(nombreUsuario) != null;
    }

    public boolean existeDPI(String dpi) throws SQLException {
        return empleadoDAO.encontrarPorDPI(dpi) != null;
    }

}

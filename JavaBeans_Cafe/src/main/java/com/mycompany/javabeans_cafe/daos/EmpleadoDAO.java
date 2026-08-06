package com.mycompany.javabeans_cafe.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EmpleadoRol;
import com.mycompany.javabeans_cafe.enums.JornadaLaboral;
import com.mycompany.javabeans_cafe.modelos.Empleado;

public class EmpleadoDAO {

    public Empleado encontrarPorNombreUsuario(String nombreUsuario) throws SQLException {
        String query = "SELECT * FROM empleado WHERE nombre_usuario = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nombreUsuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int codigoEmpleado = resultSet.getInt("codigo_empleado");
                String dpi = resultSet.getString("dpi");
                String nombreCompleto = resultSet.getString("nombre_completo");
                String contrasena = resultSet.getString("contrasena");
                EmpleadoRol rol = obtenerRolEmpleado(resultSet.getString("rol"));
                JornadaLaboral jornadaLaboral = obtenerJornadaLaboral(resultSet.getString("jornada_laboral"));
                BigDecimal salario = resultSet.getBigDecimal("salario");
                LocalDate fechaContratacion = resultSet.getDate("fecha_de_contratacion").toLocalDate();
                boolean activo = resultSet.getBoolean("activo");

                Empleado empleado = new Empleado(codigoEmpleado, dpi, nombreCompleto, nombreUsuario, contrasena, rol,
                        jornadaLaboral, salario, fechaContratacion, activo);
                return empleado;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener datos SQL de empleado por nombre: " + e.getMessage());
        }
        return null;
    }

    public Empleado encontrarPorDPI(String dpi) throws SQLException {
        String query = "SELECT * FROM empleado WHERE dpi = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, dpi);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String nombreUsuario = resultSet.getString("nombre_usuario");
                String contrasena = resultSet.getString("contrasena");
                int codigoEmpleado = resultSet.getInt("codigo_empleado");
                String nombreCompleto = resultSet.getString("nombre_completo");
                EmpleadoRol rol = obtenerRolEmpleado(resultSet.getString("rol"));
                JornadaLaboral jornadaLaboral = obtenerJornadaLaboral(resultSet.getString("jornada_laboral"));
                BigDecimal salario = resultSet.getBigDecimal("salario");
                LocalDate fechaContratacion = resultSet.getDate("fecha_de_contratacion").toLocalDate();
                boolean activo = resultSet.getBoolean("activo");

                Empleado empleado = new Empleado(codigoEmpleado, dpi, nombreCompleto, nombreUsuario, contrasena, rol,
                        jornadaLaboral, salario, fechaContratacion, activo);
                return empleado;
            }
        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener datos SQL de empleado por DPI: " + e.getMessage());
        }
        return null;
    }

    // CREATE TABLE

    // empleado(
    // codigo_empleado INT PRIMARY KEY AUTO_INCREMENT,
    // dpi VARCHAR(20) NOT NULL UNIQUE,
    // nombre_completo VARCHAR(225) NOT NULL,
    // nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    // contrasena VARCHAR(255) NOT NULL,
    // rol VARCHAR(50) NOT NULL,
    // jornada_laboral VARCHAR(50) NOT NULL,
    // salario DECIMAL(10, 2) NOT NULL,
    // fecha_de_contratacion DATE NOT NULL,
    // activo BOOLEAN NOT NULL
    // );

    private EmpleadoRol obtenerRolEmpleado(String rolString) throws SQLException {
        if (EmpleadoRol.ADMINISTRADOR.getTipoEmpleado().equals(rolString)) {
            return EmpleadoRol.ADMINISTRADOR;
        } else if (EmpleadoRol.BARISTA.getTipoEmpleado().equals(rolString)) {
            return EmpleadoRol.BARISTA;
        } else if (EmpleadoRol.COCINA.getTipoEmpleado().equals(rolString)) {
            return EmpleadoRol.COCINA;
        } else if (EmpleadoRol.MESERO.getTipoEmpleado().equals(rolString)) {
            return EmpleadoRol.MESERO;
        }
        return null;
    }

    private JornadaLaboral obtenerJornadaLaboral(String jornadaString) throws SQLException {
        if (JornadaLaboral.MATUTINA.getJornada().equals(jornadaString)) {
            return JornadaLaboral.MATUTINA;
        } else if (JornadaLaboral.VESPERTINA.getJornada().equals(jornadaString)) {
            return JornadaLaboral.VESPERTINA;
        } else if (JornadaLaboral.NOCTURNA.getJornada().equals(jornadaString)) {
            return JornadaLaboral.NOCTURNA;
        }
        return null;
    }

}

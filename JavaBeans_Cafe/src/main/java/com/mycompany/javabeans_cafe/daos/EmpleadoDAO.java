package com.mycompany.javabeans_cafe.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EmpleadoRol;
import com.mycompany.javabeans_cafe.enums.JornadaLaboral;
import com.mycompany.javabeans_cafe.modelos.Empleado;

public class EmpleadoDAO {

    public void insertarEmpleado(Empleado empleado) throws SQLException {
        String query = "INSERT INTO empleado (dpi, nombre_completo, nombre_usuario, contrasena, rol, jornada_laboral, salario, fecha_de_contratacion, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, empleado.getDpi());
            preparedStatement.setString(2, empleado.getNombreCompleto());
            preparedStatement.setString(3, empleado.getNombreUsuario());
            preparedStatement.setString(4, empleado.getContrasena());
            preparedStatement.setString(5, empleado.getRol().getTipoEmpleado());
            preparedStatement.setString(6, empleado.getJornadaLaboral().getJornada());
            preparedStatement.setBigDecimal(7, empleado.getSalario());
            preparedStatement.setDate(8, Date.valueOf(empleado.getFechaContratacion()));
            preparedStatement.setBoolean(9, empleado.isActivo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar empleado en la base de datos: " + e.getMessage());
        }
    }

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

    // PUeda que solo sea para activar y desactivar empleados
    public List<Empleado> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM empleado";

        List<Empleado> empleados = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                empleados.add(convertirAEmpleado(resultSet));
            }

        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener empleados: " + e.getMessage(),
                    e);
        }

        return empleados;
    }

    public List<Empleado> obtenerTodos(boolean activo) throws SQLException {

        String query = "SELECT * FROM empleado WHERE activo = ?";
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setBoolean(1, activo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    empleados.add(convertirAEmpleado(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener empleados: " + e.getMessage(),
                    e);
        }
        return empleados;
    }

    public List<Empleado> obtenerTodosExceptoRol(EmpleadoRol rolExcluido)
            throws SQLException {

        String query = "SELECT * FROM empleado WHERE rol <> ?";
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, rolExcluido.getTipoEmpleado());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    empleados.add(convertirAEmpleado(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener empleados excluyendo el rol " + rolExcluido + ": " + e.getMessage(), e);
        }
        return empleados;
    }

    public void actualizarEmpleado(Empleado empleado) throws SQLException {
        String query = "UPDATE empleado SET dpi = ?, nombre_completo = ?, nombre_usuario = ?, contrasena = ?, rol = ?, jornada_laboral = ?, salario = ? WHERE codigo_empleado = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, empleado.getDpi());
            preparedStatement.setString(2, empleado.getNombreCompleto());
            preparedStatement.setString(3, empleado.getNombreUsuario());
            preparedStatement.setString(4, empleado.getContrasena());
            preparedStatement.setString(5, empleado.getRol().getTipoEmpleado());
            preparedStatement.setString(6, empleado.getJornadaLaboral().getJornada());
            preparedStatement.setBigDecimal(7, empleado.getSalario());
            preparedStatement.setInt(8, empleado.getCodigoEmpleado());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar empleado en la base de datos: " + e.getMessage());
        }
    }

    public void actualizarEstadoEmpleado(int codigoEmpleado, boolean nuevoEstado) throws SQLException {
        String query = "UPDATE empleado SET activo = ? WHERE codigo_empleado = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setBoolean(1, nuevoEstado);
            preparedStatement.setInt(2, codigoEmpleado);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el estado del empleado en la base de datos: " + e.getMessage());
        }
    }

    private Empleado convertirAEmpleado(ResultSet resultSet)
            throws SQLException {

        return new Empleado(
                resultSet.getInt("codigo_empleado"),
                resultSet.getString("dpi"),
                resultSet.getString("nombre_completo"),
                resultSet.getString("nombre_usuario"),
                resultSet.getString("contrasena"),
                obtenerRolEmpleado(resultSet.getString("rol")),
                obtenerJornadaLaboral(
                        resultSet.getString("jornada_laboral")),
                resultSet.getBigDecimal("salario"),
                resultSet.getDate("fecha_de_contratacion").toLocalDate(),
                resultSet.getBoolean("activo"));
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

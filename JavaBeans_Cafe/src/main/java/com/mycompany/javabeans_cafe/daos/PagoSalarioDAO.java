package com.mycompany.javabeans_cafe.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EstadoPagoEmpleado;
import com.mycompany.javabeans_cafe.enums.TipoPago;
import com.mycompany.javabeans_cafe.modelos.PagoSalario;

public class PagoSalarioDAO {

    public void insertar(PagoSalario pagoSalario) throws SQLException {
        String query = """
                INSERT INTO pago_salario ( codigo_empleado, dpi_empleado, fecha_hora_emision, tipo_pago,
                monto_pago,estado) VALUES (?, ?, ?, ?, ?, ?)
                        """;
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, pagoSalario.getCodigoEmpleado());
            preparedStatement.setString(2, pagoSalario.getDpiEmpleado());
            preparedStatement.setTimestamp(3, pagoSalario.getFechaHoraEmision());
            preparedStatement.setString(4, pagoSalario.getTipoPago().name());
            preparedStatement.setBigDecimal(5, pagoSalario.getMontoPago());
            preparedStatement.setString(6, pagoSalario.getEstado().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el pago de salario en la base de datos: " + e.getMessage());
        }
    }

    public List<PagoSalario> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM pago_salario";
        List<PagoSalario> pagos = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                pagos.add(convertirAPagoSalario(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener los pagos de salario: "
                            + e.getMessage());
        }
        return pagos;
    }

    public void actualizarEstado(Connection conexion, int codigoNomina, EstadoPagoEmpleado nuevoEstado)
            throws SQLException {
        String query = "UPDATE pago_salario SET estado = ? WHERE codigo_nomina = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nuevoEstado.name());
            preparedStatement.setInt(2, codigoNomina);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el estado del pago de salario: " + e.getMessage());
        }
    }

    public List<PagoSalario> obtenerPendientesPorTipo(Connection conexion, TipoPago tipoPago) throws SQLException {
        String query = "SELECT * FROM pago_salario WHERE estado = ? AND tipo_pago = ?";
        List<PagoSalario> pagosPendientes = new ArrayList<>();

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, EstadoPagoEmpleado.PENDIENTE.name());
            preparedStatement.setString(2, tipoPago.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    pagosPendientes.add(convertirAPagoSalario(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los pagos pendientes: " + e.getMessage());
        }

        return pagosPendientes;
    }

    public boolean existePagoDelPeriodo(int codigoEmpleado, TipoPago tipoPago, LocalDate fecha) throws SQLException {
        String query = "SELECT 1 FROM pago_salario WHERE codigo_empleado = ? AND tipo_pago = ? AND YEAR(fecha_hora_emision) = ? AND MONTH(fecha_hora_emision) = ? LIMIT 1";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoEmpleado);
            preparedStatement.setString(2, tipoPago.name());
            preparedStatement.setInt(3, fecha.getYear());
            preparedStatement.setInt(4, fecha.getMonthValue());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {

            throw new SQLException("Error al verificar si el pago ya fue generado: " + e.getMessage());
        }
    }

    private PagoSalario convertirAPagoSalario(
            ResultSet resultSet) throws SQLException {

        return new PagoSalario(
                resultSet.getInt("codigo_nomina"),
                resultSet.getInt("codigo_empleado"),
                resultSet.getString("dpi_empleado"),
                resultSet.getTimestamp("fecha_hora_emision"),
                TipoPago.valueOf(resultSet.getString("tipo_pago")),
                resultSet.getBigDecimal("monto_pago"),
                EstadoPagoEmpleado.valueOf(resultSet.getString("estado")));
    }
}
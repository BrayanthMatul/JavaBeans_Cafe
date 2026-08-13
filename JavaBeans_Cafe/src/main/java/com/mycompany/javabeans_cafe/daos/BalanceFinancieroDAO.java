package com.mycompany.javabeans_cafe.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mycompany.javabeans_cafe.modelos.BalanceFinanciero;

public class BalanceFinancieroDAO {

    public void insertar(Connection conexion, BalanceFinanciero balanceFinanciero)
            throws SQLException {
        String query = "INSERT INTO balance_financiero (fecha_hora, monto_ingresos, monto_egresos, balance) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, balanceFinanciero.getFechaHora());
            preparedStatement.setBigDecimal(2, balanceFinanciero.getMontoIngresos());
            preparedStatement.setBigDecimal(3, balanceFinanciero.getMontoEgresos());
            preparedStatement.setBigDecimal(4, balanceFinanciero.getBalance());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error al insertar el pedido en la base de datos: " + e.getMessage());
        }
    }
}
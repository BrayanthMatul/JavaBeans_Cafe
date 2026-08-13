package com.mycompany.javabeans_cafe.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
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

    public List<BalanceFinanciero> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM balance_financiero";

        List<BalanceFinanciero> balances = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                balances.add(convertirABalanceFinanciero(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener los balances financieros: "
                            + e.getMessage());
        }
        return balances;
    }

    public BalanceFinanciero obtenerUltimo() throws SQLException {
        String query = "SELECT * FROM balance_financiero ORDER BY id DESC LIMIT 1";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                return convertirABalanceFinanciero(resultSet);
            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener el último balance financiero: " + e.getMessage());
        }

        return null;
    }

    private BalanceFinanciero convertirABalanceFinanciero(ResultSet resultSet) throws SQLException {
        return new BalanceFinanciero(
                resultSet.getInt("id"),
                resultSet.getTimestamp("fecha_hora"),
                resultSet.getBigDecimal("monto_ingresos"),
                resultSet.getBigDecimal("monto_egresos"),
                resultSet.getBigDecimal("balance"));
    }

}
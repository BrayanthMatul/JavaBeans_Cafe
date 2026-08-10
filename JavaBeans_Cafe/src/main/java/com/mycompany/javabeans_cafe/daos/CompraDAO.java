package com.mycompany.javabeans_cafe.daos;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.modelos.Compra;

public class CompraDAO {

    public void insertarCompra(Connection conexion, Compra compra) throws SQLException {
        String query = "INSERT INTO compra (codigo_insumo, fecha_hora, cantidad, monto, contabilizado) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, compra.getCodigoInsumo());
            preparedStatement.setTimestamp(2, compra.getFechaCompra());
            preparedStatement.setInt(3, compra.getCantidad());
            preparedStatement.setBigDecimal(4, compra.getMonto());
            preparedStatement.setBoolean(5, compra.isContabilizado());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar la compra en la base de datos: " + e.getMessage());
        }
    }

    public List<Compra> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM compra";

        List<Compra> compras = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                compras.add(convertirACompra(resultSet));
            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener las compras: " + e.getMessage());
        }

        return compras;
    }

    public List<Compra> obtenerPorRangoFecha(Timestamp fechaInicio, Timestamp fechaFinal) throws SQLException {
        String query = "SELECT * FROM compra WHERE fecha_hora BETWEEN ? AND ?";
        List<Compra> compras = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, fechaInicio);
            preparedStatement.setTimestamp(2, fechaFinal);

            try (ResultSet resultSet =
                    preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    compras.add(convertirACompra(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener las compras por rango de fechas: " + e.getMessage());
        }

        return compras;
    }

    private Compra convertirACompra(ResultSet resultSet) throws SQLException {
        return new Compra(
                resultSet.getInt("codigo_compra"),
                resultSet.getInt("codigo_insumo"),
                resultSet.getTimestamp("fecha_hora"),
                resultSet.getInt("cantidad"),
                resultSet.getBigDecimal("monto"),
                resultSet.getBoolean("contabilizado")
        );
    }
}
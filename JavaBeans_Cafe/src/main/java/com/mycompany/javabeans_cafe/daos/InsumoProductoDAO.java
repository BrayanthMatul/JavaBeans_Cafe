package com.mycompany.javabeans_cafe.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.modelos.InsumoProducto;

public class InsumoProductoDAO {

    public void insertar(InsumoProducto insumoProducto) throws SQLException {
        String query = "INSERT INTO insumo_producto (codigo_insumo, codigo_producto, cantidad) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, insumoProducto.getCodigoInsumo());
            preparedStatement.setInt(2, insumoProducto.getCodigoProducto());
            preparedStatement.setBigDecimal(3, insumoProducto.getCantidad());
            preparedStatement.executeUpdate();
        }
    }

    public List<InsumoProducto> obtenerInsumosPorProducto(Connection conexion, int codigoProducto) throws SQLException {
        List<InsumoProducto> insumosProducto = new ArrayList<>();
        String query = "SELECT * FROM insumo_producto WHERE codigo_producto = ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoProducto);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    insumosProducto.add(convertirAInsumoProducto(resultSet));
                }
            }
        }
        return insumosProducto;
    }

    public BigDecimal obtenerCantidadInsumoPorProducto(int codigoProducto, int codigoInsumo) throws SQLException {
        String query = "SELECT cantidad FROM insumo_producto WHERE codigo_producto = ? AND codigo_insumo = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoProducto);
            preparedStatement.setInt(2, codigoInsumo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("cantidad");
                }
            }
        }
        return null;
    }

    public void actualizarCantidad(int codigoProducto, int codigoInsumo, BigDecimal nuevaCantidad) throws SQLException {
        String query = "UPDATE insumo_producto SET cantidad = ? WHERE codigo_producto = ? AND codigo_insumo = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, nuevaCantidad);
            preparedStatement.setInt(2, codigoProducto);
            preparedStatement.setInt(3, codigoInsumo);
            preparedStatement.executeUpdate();
        }
    }

    private InsumoProducto convertirAInsumoProducto(ResultSet resultSet) throws SQLException {
        return new InsumoProducto(
                resultSet.getInt("id"),
                resultSet.getInt("codigo_insumo"),
                resultSet.getInt("codigo_producto"),
                resultSet.getBigDecimal("cantidad"));
    }

}

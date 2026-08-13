package com.mycompany.javabeans_cafe.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.modelos.ProductoPedido;

public class ProductoPedidoDAO {

    // Pendiente de eliminar, por si acaso se necesita en el futuro
    private void insertar(ProductoPedido productoPedido) throws SQLException {

        String query = "INSERT INTO producto_pedido (codigo_producto, codigo_pedido, cantidad, subtotal) VALUES (?, ?, ?, ?) ";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(
                        query)) {

            preparedStatement.setInt(1, productoPedido.getCodigoProducto());
            preparedStatement.setInt(2, productoPedido.getCodigoPedido());
            preparedStatement.setInt(3, productoPedido.getCantidad());
            preparedStatement.setBigDecimal(4, productoPedido.getSubtotal());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el producto en el pedido: " + e.getMessage());
        }
    }

    public List<ProductoPedido> obtenerPorPedido(int codigoPedido) throws SQLException {
        String query = "SELECT * FROM producto_pedido WHERE codigo_pedido = ? ORDER BY id";
        List<ProductoPedido> productosPedido = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setInt(1, codigoPedido);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    productosPedido.add(
                            convertirAProductoPedido(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los productos del pedido: " + e.getMessage());
        }
        return productosPedido;
    }

    public ProductoPedido obtenerPorProductoYPedido(int codigoProducto, int codigoPedido) throws SQLException {
        String query = "SELECT * FROM producto_pedido WHERE codigo_producto = ? AND codigo_pedido = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoProducto);
            preparedStatement.setInt(2, codigoPedido);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return convertirAProductoPedido(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al buscar el producto en el pedido: " + e.getMessage());
        }
        return null;
    }

    public void actualizarCantidadYSubtotal(int id, int nuevaCantidad, BigDecimal nuevoSubtotal) throws SQLException {
        String query = "UPDATE producto_pedido SET cantidad = ?, subtotal = ? WHERE id = ? ";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, nuevaCantidad);
            preparedStatement.setBigDecimal(2, nuevoSubtotal);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el producto del pedido: " + e.getMessage());
        }
    }

    public void eliminar(int id) throws SQLException {
        String query = "DELETE FROM producto_pedido WHERE id = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al eliminar el producto del pedido: " + e.getMessage());
        }
    }

    public void agregarOAcumular(Connection conexion, ProductoPedido productoPedido) throws SQLException {
        String query = """
                INSERT INTO producto_pedido (codigo_producto, codigo_pedido, cantidad, subtotal) VALUES (?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE cantidad = cantidad + VALUES(cantidad), subtotal = subtotal + VALUES(subtotal)
                """;

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, productoPedido.getCodigoProducto());
            preparedStatement.setInt(2, productoPedido.getCodigoPedido());
            preparedStatement.setInt(3, productoPedido.getCantidad());
            preparedStatement.setBigDecimal(4, productoPedido.getSubtotal());
            preparedStatement.executeUpdate();
        }
    }

    private ProductoPedido convertirAProductoPedido(ResultSet resultSet) throws SQLException {
        return new ProductoPedido(
                resultSet.getInt("id"),
                resultSet.getInt("codigo_producto"),
                resultSet.getInt("codigo_pedido"),
                resultSet.getInt("cantidad"),
                resultSet.getBigDecimal("subtotal"));
    }

}

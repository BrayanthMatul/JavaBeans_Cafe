package com.mycompany.javabeans_cafe.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EstadoCuentaPedido;
import com.mycompany.javabeans_cafe.modelos.ProductoMasVendido;

public class ProductoMasVendidoDAO {

    public List<ProductoMasVendido> obtenerProductosMasVendidos()
            throws SQLException {

        String query = """
                SELECT codigo_producto, SUM(cantidad) AS cantidad_total FROM producto_pedido
                WHERE codigo_pedido IN (SELECT codigo_pedido FROM pedido WHERE estado_cuenta = ?)
                GROUP BY codigo_producto ORDER BY cantidad_total DESC
                """;

        List<Integer> codigosProductos = new ArrayList<>();
        List<Integer> cantidadesTotales = new ArrayList<>();

        List<ProductoMasVendido> productosMasVendidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setString(1, EstadoCuentaPedido.PAGADA.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    codigosProductos.add(resultSet.getInt("codigo_producto"));
                    cantidadesTotales.add(resultSet.getInt("cantidad_total"));
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los productos más vendidos: " + e.getMessage());
        }

        ProductoMenuDAO productoMenuDAO = new ProductoMenuDAO();

        for (int i = 0; i < codigosProductos.size(); i++) {
            int codigoProducto = codigosProductos.get(i);
            int cantidadTotal = cantidadesTotales.get(i);
            String nombreProducto = productoMenuDAO.obtenerNombreProductoPorCodigo(codigoProducto);

            ProductoMasVendido producto = new ProductoMasVendido(
                    codigoProducto,
                    nombreProducto,
                    cantidadTotal);

            productosMasVendidos.add(producto);
        }
        return productosMasVendidos;
    }
}

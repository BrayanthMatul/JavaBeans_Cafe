package com.mycompany.javabeans_cafe.daos;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.CategoriaProducto;
import com.mycompany.javabeans_cafe.modelos.ProductoMenu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoMenuDAO {

    public void insertarProducto(ProductoMenu producto) throws SQLException {
        String query = "INSERT INTO producto_menu (nombre_producto, categoria, precio_venta, imagen) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, producto.getNombreProducto());
            preparedStatement.setString(2, producto.getCategoria().name());
            preparedStatement.setBigDecimal(3, producto.getPrecioVenta());
            preparedStatement.setBytes(4, producto.getImagen());
            preparedStatement.executeUpdate();
        }
    }

    public ProductoMenu encontrarPorNombre(String nombreProducto) throws SQLException {
        String query = "SELECT * FROM producto_menu WHERE nombre_producto = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nombreProducto);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return convertirAProducto(resultSet);
                }
            }
        }
        return null;
    }

    public List<ProductoMenu> obtenerTodos() throws SQLException {
        List<ProductoMenu> productos = new ArrayList<>();
        String query = "SELECT * FROM producto_menu";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                productos.add(convertirAProducto(resultSet));
            }
        }
        return productos;
    }

    public void actualizarProducto(ProductoMenu producto) throws SQLException {
        String query = "UPDATE producto_menu SET nombre_producto = ?, categoria = ?, precio_venta = ?, imagen = ? WHERE codigo_producto = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setString(1, producto.getNombreProducto());
            preparedStatement.setString(2, producto.getCategoria().name());
            preparedStatement.setBigDecimal(3, producto.getPrecioVenta());
            preparedStatement.setBytes(4, producto.getImagen());
            preparedStatement.setInt(5, producto.getCodigoProducto());
            preparedStatement.executeUpdate();
        }
    }

    public List<ProductoMenu> obtenerProductosPorCategoria(CategoriaProducto categoria) throws SQLException {
        List<ProductoMenu> productosFiltrados = new ArrayList<>();
        String query = "SELECT * FROM producto_menu WHERE categoria = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, categoria.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    productosFiltrados.add(convertirAProducto(resultSet));
                }

            }
        }
        return productosFiltrados;
    }

    private ProductoMenu convertirAProducto(ResultSet resultSet) throws SQLException {
        return new ProductoMenu(
                resultSet.getInt("codigo_producto"),
                resultSet.getString("nombre_producto"),
                obtenerCategoriaProducto(resultSet.getString("categoria")),
                resultSet.getBigDecimal("precio_venta"),
                resultSet.getBytes("imagen"));
    }

    private CategoriaProducto obtenerCategoriaProducto(String categoria) throws SQLException {
        try {
            return CategoriaProducto.valueOf(categoria);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Categoría de producto no válida: " + categoria, e);
        }
    }

}

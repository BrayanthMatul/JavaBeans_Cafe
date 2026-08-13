package com.mycompany.javabeans_cafe.daos;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.exceptions.StockInsuficienteException;
import com.mycompany.javabeans_cafe.modelos.Insumo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsumoDAO {

    public void insertarInsumo(Insumo insumo) throws SQLException {
        String query = "INSERT INTO insumo (nombre_insumo, unidad_medida, stock_actual, stock_minimo, costo_insumo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, insumo.getNombreInsumo());
            preparedStatement.setString(2, insumo.getUnidadMedida());
            preparedStatement.setInt(3, insumo.getStockActual());
            preparedStatement.setInt(4, insumo.getStockMinimo());
            preparedStatement.setBigDecimal(5, insumo.getCostoInsumo());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error al insertar insumo en la base de datos: " + e.getMessage());
        }
    }

    public Insumo obtenerPorNombre(String nombreInsumo) throws SQLException {
        String query = "SELECT * FROM insumo WHERE nombre_insumo = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nombreInsumo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return convertirAInsumo(resultSet);
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener insumo por nombre: " + e.getMessage());
        }
        return null;
    }

    public List<Insumo> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM insumo";
        List<Insumo> insumos = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                insumos.add(convertirAInsumo(resultSet));
            }

        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener insumos: "
                            + e.getMessage());
        }
        return insumos;
    }

    public void actualizarInsumo(Insumo insumo) throws SQLException {
        String query = "UPDATE insumo SET nombre_insumo = ?, unidad_medida = ?, stock_actual = ?, stock_minimo = ?, costo_insumo = ? WHERE codigo_insumo = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, insumo.getNombreInsumo());
            preparedStatement.setString(2, insumo.getUnidadMedida());
            preparedStatement.setInt(3, insumo.getStockActual());
            preparedStatement.setInt(4, insumo.getStockMinimo());
            preparedStatement.setBigDecimal(5, insumo.getCostoInsumo());
            preparedStatement.setInt(6, insumo.getCodigoInsumo());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el insumo en la base de datos: " + e.getMessage());
        }
    }

    public void disminuirStock(Connection conexion, int codigoInsumo, BigDecimal cantidad)
            throws SQLException, StockInsuficienteException {
        String query = "UPDATE insumo SET stock_actual = stock_actual - ? WHERE codigo_insumo = ? AND stock_actual >= ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, cantidad);
            preparedStatement.setInt(2, codigoInsumo);
            preparedStatement.setBigDecimal(3, cantidad);
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas == 0) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para confirmar la operación. No se pudo disminuir el stock del insumo con código: "
                                + codigoInsumo);
            }

        } catch (SQLException e) {
            throw new SQLException(
                    "Error al disminuir el stock del insumo: "
                            + e.getMessage());
        }
    }

    public void aumentarStock(Connection conexion, int codigoInsumo, int cantidad) throws SQLException {
        String query = "UPDATE insumo SET stock_actual = stock_actual + ? WHERE codigo_insumo = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setInt(2, codigoInsumo);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al aumentar el stock del insumo: " + e.getMessage());
        }
    }

    public List<Insumo> obtenerConStockBajo() throws SQLException {
        String query = "SELECT * FROM insumo WHERE stock_actual <= stock_minimo";
        List<Insumo> insumos = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                insumos.add(convertirAInsumo(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los insumos con bajo stock: " + e.getMessage());
        }
        return insumos;
    }

    public BigDecimal obtenerStockParaActualizar(Connection conexion, int codigoInsumo) throws SQLException {
        String query = "SELECT stock_actual FROM insumo WHERE codigo_insumo = ? FOR UPDATE ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoInsumo);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("stock_actual");
                }
                throw new SQLException("No se encontró el insumo con código " + codigoInsumo);
            }
        }
    }

    private Insumo convertirAInsumo(ResultSet resultSet) throws SQLException {
        return new Insumo(
                resultSet.getInt("codigo_insumo"),
                resultSet.getString("nombre_insumo"),
                resultSet.getString("unidad_medida"),
                resultSet.getInt("stock_actual"),
                resultSet.getInt("stock_minimo"),
                resultSet.getBigDecimal("costo_insumo"));
    }
}
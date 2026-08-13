package com.mycompany.javabeans_cafe.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EstadoMesa;
import com.mycompany.javabeans_cafe.modelos.Mesa;

public class MesaDAO {

    public void insertarMesa(Mesa mesa) throws SQLException {
        String query = "INSERT INTO mesa (capacidad, estado) VALUES (?, ?)";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, mesa.getCapacidad());
            preparedStatement.setString(2, mesa.getEstado().name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar la mesa en la base de datos: " + e.getMessage());
        }
    }

    public List<Mesa> obtenerTodos() throws SQLException {
        String query = "SELECT * FROM mesa";
        List<Mesa> mesas = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                mesas.add(convertirAMesa(resultSet));
            }
        } catch (SQLException e) {
            throw new SQLException("Error al obtener las mesas: " + e.getMessage());
        }
        return mesas;
    }

    public void actualizarCapacidad(int numeroMesa, int nuevaCapacidad) throws SQLException {
        String query = "UPDATE mesa SET capacidad = ? WHERE numero_mesa = ?";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, nuevaCapacidad);
            preparedStatement.setInt(2, numeroMesa);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la capacidad de la mesa: " + e.getMessage());
        }
    }

    public void actualizarEstado(Connection conexion, int numeroMesa, EstadoMesa nuevoEstado) throws SQLException {
        String query = "UPDATE mesa SET estado = ? WHERE numero_mesa = ?";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nuevoEstado.name());
            preparedStatement.setInt(2, numeroMesa);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el estado de la mesa: " + e.getMessage());
        }
    }

    private Mesa convertirAMesa(ResultSet resultSet) throws SQLException {
        return new Mesa(
                resultSet.getInt("numero_mesa"),
                resultSet.getInt("capacidad"),
                EstadoMesa.valueOf(
                        resultSet.getString("estado")));
    }
}
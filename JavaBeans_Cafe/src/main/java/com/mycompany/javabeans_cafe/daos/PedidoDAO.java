package com.mycompany.javabeans_cafe.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EstadoCuentaPedido;
import com.mycompany.javabeans_cafe.modelos.Pedido;

public class PedidoDAO {

    public int insertar(Pedido pedido) throws SQLException {
        String query = """
                INSERT INTO pedido (codigo_empleado, numero_mesa, fecha_hora_ocupacion, fecha_hora_liberacion,
                propina, monto_pedido, estado_cuenta, contabilizado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, pedido.getCodigoEmpleado());
            preparedStatement.setInt(2, pedido.getNumeroMesa());
            preparedStatement.setTimestamp(3, pedido.getFechaHoraOcupacion());
            preparedStatement.setTimestamp(4, pedido.getFechaHoraLiberacion());
            preparedStatement.setBigDecimal(5, pedido.getPropina());
            preparedStatement.setBigDecimal(6, pedido.getMontoPedido());
            preparedStatement.setString(7, pedido.getEstadoCuenta().name());
            preparedStatement.setBoolean(8, pedido.isContabilizado());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el ID generado para el pedido.");
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error al insertar el pedido en la base de datos: " + e.getMessage());
        }
    }

    public Pedido obtenerPedidoAbiertoPorMesa(int numeroMesa) throws SQLException {
        String query = "SELECT * FROM pedido WHERE numero_mesa = ? AND estado_cuenta = ?";
        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, numeroMesa);
            preparedStatement.setString(2, EstadoCuentaPedido.ABIERTA.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return convertirAPedido(resultSet);
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener el pedido abierto por mesa: " + e.getMessage());
        }
    }

    public List<Pedido> obtenerAbiertosPorEmpleado(int codigoEmpleado) throws SQLException {
        String query = "SELECT * FROM pedido WHERE codigo_empleado = ? AND estado_cuenta = ?";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoEmpleado);
            preparedStatement.setString(2, EstadoCuentaPedido.ABIERTA.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    pedidos.add(convertirAPedido(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener los pedidos abiertos del empleado: " + e.getMessage());
        }

        return pedidos;
    }

    public List<Pedido> obtenerNoContabilizados() throws SQLException {
        String query = "SELECT * FROM pedido WHERE contabilizado = FALSE AND estado_cuenta = ? ";
        List<Pedido> pedidos = new ArrayList<>();

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, EstadoCuentaPedido.PAGADA.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    pedidos.add(convertirAPedido(resultSet));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error al obtener los pedidos no contabilizados: " + e.getMessage());
        }

        return pedidos;
    }

    public void agregarHoraLiberacion(Connection conexion, int codigoPedido, Timestamp fechaHoraLiberacion)
            throws SQLException {

        String query = " UPDATE pedido SET fecha_hora_liberacion = ? WHERE codigo_pedido = ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setTimestamp(1, fechaHoraLiberacion);
            preparedStatement.setInt(2, codigoPedido);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al registrar la hora de liberación del pedido: " + e.getMessage());
        }
    }

    public void marcarTodosContabilizados(Connection conexion) throws SQLException {
        String query = "UPDATE pedido SET contabilizado = TRUE WHERE contabilizado = FALSE AND estado_cuenta = ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, EstadoCuentaPedido.PAGADA.name());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Error al marcar los pedidos como contabilizados: " + e.getMessage());
        }
    }

    public void actualizarEstado(Connection conexion, int codigoPedido, EstadoCuentaPedido nuevoEstado)
            throws SQLException {
        String query = " UPDATE pedido SET estado_cuenta = ? WHERE codigo_pedido = ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, nuevoEstado.name());
            preparedStatement.setInt(2, codigoPedido);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el estado del pedido: " + e.getMessage());
        }
    }

    public void actualizarMontoPedido(Connection conexion, int codigoPedido, BigDecimal nuevoMonto)
            throws SQLException {

        String query = "UPDATE pedido SET monto_pedido = ? WHERE codigo_pedido = ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setBigDecimal(1, nuevoMonto);
            preparedStatement.setInt(2, codigoPedido);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el monto del pedido: " + e.getMessage());
        }
    }

    public void actualizarPropina(int codigoPedido, BigDecimal nuevaPropina) throws SQLException {
        String query = "UPDATE pedido SET propina = ? WHERE codigo_pedido = ? ";

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {

            preparedStatement.setBigDecimal(1, nuevaPropina);
            preparedStatement.setInt(2, codigoPedido);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar la propina del pedido: " + e.getMessage());
        }
    }

    public BigDecimal obtenerPropinasNoContabilizadasPorEmpleado(int codigoEmpleado) throws SQLException {
        String query = """
                SELECT COALESCE(SUM(propina), 0) AS total_propinas FROM pedido
                        WHERE codigo_empleado = ? AND estado_cuenta = ? AND contabilizado = FALSE
                        """;

        try (Connection conexion = ConexionBD.getConexion();
                PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, codigoEmpleado);
            preparedStatement.setString(2, EstadoCuentaPedido.PAGADA.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("total_propinas");
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal obtenerMontoNoContabilizado(Connection conexion) throws SQLException {
        String query = """
                SELECT COALESCE(SUM(monto_pedido), 0) AS total_monto
                FROM pedido WHERE contabilizado = FALSE AND estado_cuenta = ?
                """;

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, EstadoCuentaPedido.PAGADA.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBigDecimal("total_monto");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(
                    "Error al obtener el monto de los pedidos no contabilizados: "
                            + e.getMessage());
        }

        return BigDecimal.ZERO;
    }

    public void marcarTodosContabilizado(Connection conexion) throws SQLException {
        String query = "UPDATE pedido SET contabilizado = TRUE WHERE contabilizado = FALSE AND estado_cuenta = ? ";

        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, EstadoCuentaPedido.PAGADA.name());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error al marcar el pedido como contabilizado: " + e.getMessage());
        }
    }

    private Pedido convertirAPedido(ResultSet resultSet) throws SQLException {
        return new Pedido(
                resultSet.getInt("codigo_pedido"),
                resultSet.getInt("codigo_empleado"),
                resultSet.getInt("numero_mesa"),
                resultSet.getTimestamp("fecha_hora_ocupacion"),
                resultSet.getTimestamp("fecha_hora_liberacion"),
                resultSet.getBigDecimal("propina"),
                resultSet.getBigDecimal("monto_pedido"),
                EstadoCuentaPedido.valueOf(resultSet.getString("estado_cuenta")),
                resultSet.getBoolean("contabilizado"));
    }
}

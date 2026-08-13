package com.mycompany.javabeans_cafe.servicios;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.InsumoDAO;
import com.mycompany.javabeans_cafe.daos.InsumoProductoDAO;
import com.mycompany.javabeans_cafe.daos.MesaDAO;
import com.mycompany.javabeans_cafe.daos.PedidoDAO;
import com.mycompany.javabeans_cafe.daos.ProductoPedidoDAO;
import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EstadoCuentaPedido;
import com.mycompany.javabeans_cafe.enums.EstadoMesa;
import com.mycompany.javabeans_cafe.exceptions.StockInsuficienteException;
import com.mycompany.javabeans_cafe.modelos.InsumoProducto;
import com.mycompany.javabeans_cafe.modelos.Pedido;
import com.mycompany.javabeans_cafe.modelos.ProductoPedido;
import com.mycompany.javabeans_cafe.modelos.ProductoPedidoConNombre;

public class CuentasServicio {

    private int codigoEmpleado;
    private int numeroMesa;

    public CuentasServicio() {
    }

    public CuentasServicio(int codigoEmpleado, int numeroMesa) {
        this.codigoEmpleado = codigoEmpleado;
        this.numeroMesa = numeroMesa;
    }

    public CuentasServicio(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Pedido configurarNuevaCuenta() throws SQLException {
        marcarMesaComoOcupada();
        return registrarNuevoPedido();
    }

    public Pedido recuperarCuentaAbierta() throws SQLException {
        PedidoDAO pedidoDAO = new PedidoDAO();
        return pedidoDAO.obtenerPedidoAbiertoPorMesa(numeroMesa);
    }

    public List<ProductoPedidoConNombre> agregarProductoSinConfirmar(ProductoPedidoConNombre producto,
            List<ProductoPedidoConNombre> productos) {
        String nombreProducto = producto.getNombreProducto();

        // Si el producto ya existe en la lista, se actualiza la cantidad y el monto
        for (ProductoPedidoConNombre productoListado : productos) {
            if (productoListado.getNombreProducto().equals(nombreProducto)) {
                int cantidadActual = productoListado.getProductoPedido().getCantidad();
                int cantidadNueva = producto.getProductoPedido().getCantidad();
                productoListado.getProductoPedido().setCantidad(cantidadActual + cantidadNueva);

                BigDecimal subtotalActual = productoListado.getProductoPedido().getSubtotal();
                BigDecimal subtotalNuevo = producto.getProductoPedido().getSubtotal();
                BigDecimal subtotalTotal = subtotalActual.add(subtotalNuevo);

                productoListado.getProductoPedido().setSubtotal(subtotalTotal);
                return productos;
            }
        }
        // Si el producto no existe en la lista, se agrega
        productos.add(producto);
        return productos;
    }

    public List<ProductoPedidoConNombre> eliminarProductoSinConfirmar(ProductoPedidoConNombre producto,
            List<ProductoPedidoConNombre> productos) {
        String nombreProducto = producto.getNombreProducto();

        productos.removeIf(productoListado -> productoListado.getNombreProducto().equals(nombreProducto));

        return productos;
    }

    public BigDecimal confirmarProductos(Pedido pedido, List<ProductoPedidoConNombre> productos)
            throws SQLException, StockInsuficienteException {
        if (productos == null || productos.isEmpty()) {
            return pedido.getMontoPedido();
        }

        ProductoPedidoDAO productoPedidoDAO = new ProductoPedidoDAO();
        InsumoProductoDAO insumoProductoDAO = new InsumoProductoDAO();
        InsumoDAO insumoDAO = new InsumoDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();

        try (Connection conexion = ConexionBD.getConexion()) {
            conexion.setAutoCommit(false);

            try {
                BigDecimal montoNuevo = BigDecimal.ZERO;

                for (ProductoPedidoConNombre productoConNombre : productos) {
                    ProductoPedido productoPedido = productoConNombre.getProductoPedido();
                    int codigoProducto = productoPedido.getCodigoProducto();
                    int cantidadProductos = productoPedido.getCantidad();

                    List<InsumoProducto> receta = insumoProductoDAO.obtenerInsumosPorProducto(conexion, codigoProducto);

                    for (InsumoProducto ingrediente : receta) {
                        BigDecimal cantidadPorUnidad = ingrediente.getCantidad();
                        BigDecimal cantidadNecesaria = cantidadPorUnidad
                                .multiply(BigDecimal.valueOf(cantidadProductos));
                        int codigoInsumo = ingrediente.getCodigoInsumo();
                        insumoDAO.disminuirStock(conexion, codigoInsumo, cantidadNecesaria);
                    }

                    productoPedidoDAO.agregarOAcumular(conexion, productoPedido);
                    montoNuevo = montoNuevo.add(productoPedido.getSubtotal());
                }

                pedidoDAO.actualizarMontoPedido(conexion, pedido.getCodigoPedido(), montoNuevo);
                conexion.commit();
                return pedido.getMontoPedido().add(montoNuevo);
            } catch (StockInsuficienteException e) {
                conexion.rollback();
                throw e;
            } catch (SQLException e) {
                conexion.rollback();
                throw new SQLException("No se pudieron confirmar los productos: " + e.getMessage());
            }
        }
    }

    public void cerrarCuenta(Pedido pedido) throws SQLException {
        PedidoDAO pedidoDAO = new PedidoDAO();
        MesaDAO mesaDAO = new MesaDAO();
        Timestamp fechaHoraLiberacion = new Timestamp(System.currentTimeMillis());

        try (Connection conexion = ConexionBD.getConexion()) {
            conexion.setAutoCommit(false);

            try {
                pedidoDAO.agregarHoraLiberacion(conexion, pedido.getCodigoPedido(), fechaHoraLiberacion);
                pedidoDAO.actualizarEstado(conexion, pedido.getCodigoPedido(), EstadoCuentaPedido.PAGADA);
                mesaDAO.actualizarEstado(conexion, pedido.getNumeroMesa(), EstadoMesa.LIBRE);
                conexion.commit();
                pedido.setFechaHoraLiberacion(fechaHoraLiberacion);
                pedido.setEstadoCuenta(EstadoCuentaPedido.PAGADA);
            } catch (SQLException e) {
                conexion.rollback();
                throw new SQLException("No se pudo cerrar la cuenta: " + e.getMessage());
            }
        }
    }

    private void marcarMesaComoOcupada() throws SQLException {
        MesaDAO mesaDAO = new MesaDAO();
        Connection conexion = ConexionBD.getConexion();
        mesaDAO.actualizarEstado(conexion, numeroMesa, EstadoMesa.OCUPADA);

    }

    private Pedido registrarNuevoPedido() throws SQLException {
        int codigoEmpleado = this.codigoEmpleado;
        int numeroMesa = this.numeroMesa;
        Timestamp fechaHoraOcupacion = new java.sql.Timestamp(System.currentTimeMillis());
        Timestamp fechaHoraLiberacion = null;
        BigDecimal propina = BigDecimal.ZERO;
        BigDecimal montoPedido = BigDecimal.ZERO;
        EstadoCuentaPedido estadoCuenta = EstadoCuentaPedido.ABIERTA;
        boolean contabilizado = false;

        Pedido nuevoPedido = new Pedido(codigoEmpleado, numeroMesa, fechaHoraOcupacion, fechaHoraLiberacion,
                propina, montoPedido, estadoCuenta, contabilizado);

        PedidoDAO pedidoDAO = new PedidoDAO();
        int codigoPedido = pedidoDAO.insertar(nuevoPedido);

        nuevoPedido.setCodigoPedido(codigoPedido);
        return nuevoPedido;
    }

}

package com.mycompany.javabeans_cafe.servicios;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.BalanceFinancieroDAO;
import com.mycompany.javabeans_cafe.daos.CompraDAO;
import com.mycompany.javabeans_cafe.daos.PagoSalarioDAO;
import com.mycompany.javabeans_cafe.daos.PedidoDAO;
import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.enums.EstadoPagoEmpleado;
import com.mycompany.javabeans_cafe.enums.TipoPago;
import com.mycompany.javabeans_cafe.exceptions.NoEsFechaDePagoException;
import com.mycompany.javabeans_cafe.modelos.BalanceFinanciero;
import com.mycompany.javabeans_cafe.modelos.PagoSalario;

public class BalanceServicio {

        private BigDecimal totalPagado;

        public BigDecimal getTotalPagado() {
                return totalPagado;
        }

        public List<PagoSalario> realizarBalance(TipoPago tipoPago, boolean modoPrueba)
                        throws SQLException, NoEsFechaDePagoException {

                if (!modoPrueba) {
                        verificarFechaPago(tipoPago);
                }

                PagoSalarioDAO pagoSalarioDAO = new PagoSalarioDAO();
                PedidoDAO pedidoDAO = new PedidoDAO();
                CompraDAO compraDAO = new CompraDAO();
                BalanceFinancieroDAO balanceDAO = new BalanceFinancieroDAO();

                try (Connection conexion = ConexionBD.getConexion()) {
                        conexion.setAutoCommit(false);

                        try {
                                List<PagoSalario> pagos = pagoSalarioDAO.obtenerPendientesPorTipo(conexion, tipoPago);
                                BigDecimal montoEgresos = BigDecimal.ZERO;

                                if (pagos.isEmpty()) {
                                        throw new SQLException(
                                                        "No hay pagos pendientes para el tipo de pago: " + tipoPago);
                                }

                                for (PagoSalario pago : pagos) {
                                        montoEgresos = montoEgresos.add(pago.getMontoPago());
                                        pagoSalarioDAO.actualizarEstado(conexion, pago.getCodigoNomina(),
                                                        EstadoPagoEmpleado.PAGADO);
                                        pago.setEstado(EstadoPagoEmpleado.PAGADO);
                                }

                                BigDecimal montoCompras = compraDAO.obtenerMontoNoContabilizado(conexion);

                                montoEgresos = montoEgresos.add(montoCompras);
                                BigDecimal montoIngresos = pedidoDAO.obtenerMontoNoContabilizado(conexion);

                                pedidoDAO.marcarTodosContabilizados(conexion);
                                compraDAO.marcarTodosContabilizados(conexion);
                                BigDecimal balance = montoIngresos.subtract(montoEgresos);
                                BalanceFinanciero nuevoBalance = new BalanceFinanciero(montoIngresos, montoEgresos,
                                                balance);
                                balanceDAO.insertar(conexion, nuevoBalance);
                                conexion.commit();
                                this.totalPagado = montoEgresos;
                                return pagos;

                        } catch (SQLException e) {
                                conexion.rollback();
                                throw new SQLException("Error al realizar el balance: " + e.getMessage());
                        }
                }
        }

        private void verificarFechaPago(TipoPago tipoPago) throws NoEsFechaDePagoException {
                LocalDate fechaActual = LocalDate.now();

                if (tipoPago == TipoPago.QUINCENA) {
                        if (fechaActual.getDayOfMonth() != 15) {
                                throw new NoEsFechaDePagoException(
                                                "El pago de quincena solo puede realizarse el día 15.");
                        }
                } else if (tipoPago == TipoPago.FIN_DE_MES) {
                        LocalDate ultimoDiaMes = YearMonth.from(fechaActual).atEndOfMonth();

                        if (!fechaActual.equals(ultimoDiaMes)) {
                                throw new NoEsFechaDePagoException(
                                                "El pago de fin de mes solo puede realizarse el último día del mes.");
                        }
                }
        }
}

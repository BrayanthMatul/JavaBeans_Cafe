package com.mycompany.javabeans_cafe.servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.EmpleadoDAO;
import com.mycompany.javabeans_cafe.daos.PagoSalarioDAO;
import com.mycompany.javabeans_cafe.daos.PedidoDAO;
import com.mycompany.javabeans_cafe.enums.EstadoPagoEmpleado;
import com.mycompany.javabeans_cafe.enums.TipoPago;
import com.mycompany.javabeans_cafe.modelos.Empleado;
import com.mycompany.javabeans_cafe.modelos.PagoSalario;

public class PagoPendienteAutomaticoServicio {

    public void ejecutarPagoAutomatico() throws SQLException {

        if (debeGenerarPagoQuincena()) {
            generarPagosPendientes(TipoPago.QUINCENA, new BigDecimal("0.30"));
            System.out.println("Pagos pendientes registrados");
        }

        if (debeGenerarPagoFinDeMes()) {
            generarPagosPendientes(TipoPago.FIN_DE_MES, new BigDecimal("0.70"));
            System.out.println("Pagos pendientes registrados");
        }
        
        System.out.println("No se registraron nuevos pagos pendientes");
    }

    private boolean debeGenerarPagoQuincena() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaCorte = fechaActual.withDayOfMonth(15);
        LocalDate fechaEmision = fechaCorte.minusDays(5);

        return fechaActual.isEqual(fechaEmision) || fechaActual.isAfter(fechaEmision);
    }

    private boolean debeGenerarPagoFinDeMes() {
        LocalDate fechaActual = LocalDate.now();
        YearMonth mesActual = YearMonth.from(fechaActual);
        LocalDate fechaCorte = mesActual.atEndOfMonth();
        LocalDate fechaEmision = fechaCorte.minusDays(5);

        return fechaActual.isEqual(fechaEmision) || fechaActual.isAfter(fechaEmision);
    }

    private void generarPagosPendientes(TipoPago tipoPago, BigDecimal porcentajeSalario) throws SQLException {
        EmpleadoDAO empleadoDAO = new EmpleadoDAO();
        PagoSalarioDAO pagoSalarioDAO = new PagoSalarioDAO();
        PedidoDAO pedidoDAO = new PedidoDAO();
        LocalDate fechaActual = LocalDate.now();
        List<Empleado> empleados = empleadoDAO.obtenerTodos(true);

        for (Empleado empleado : empleados) {
            boolean pagoYaExiste = pagoSalarioDAO.existePagoDelPeriodo(empleado.getCodigoEmpleado(), tipoPago,
                    fechaActual);

            if (pagoYaExiste) {
                continue;
            }

            BigDecimal montoPago = empleado.getSalario().multiply(porcentajeSalario).setScale(2, RoundingMode.HALF_UP);
            BigDecimal propinas = pedidoDAO.obtenerPropinasNoContabilizadasPorEmpleado(empleado.getCodigoEmpleado());
            montoPago = montoPago.add(propinas);

            PagoSalario nuevoPago = new PagoSalario(empleado.getCodigoEmpleado(),
                    empleado.getDpi(),
                    new Timestamp(System.currentTimeMillis()),
                    tipoPago,
                    montoPago,
                    EstadoPagoEmpleado.PENDIENTE);

            pagoSalarioDAO.insertar(nuevoPago);
        }
    }
}
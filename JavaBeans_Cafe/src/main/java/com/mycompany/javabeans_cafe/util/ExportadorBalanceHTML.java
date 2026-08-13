package com.mycompany.javabeans_cafe.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.BalanceFinancieroDAO;
import com.mycompany.javabeans_cafe.modelos.BalanceFinanciero;

public class ExportadorBalanceHTML {

    private List<BalanceFinanciero> balances;
    private BalanceFinancieroDAO balanceFinancieroDAO;

    public void exportar(File archivo) throws IOException, SQLException {
        this.balanceFinancieroDAO = new BalanceFinancieroDAO();
        this.balances = balanceFinancieroDAO.obtenerTodos();

        String stringHTML = generarHTML();

        Files.writeString(
                archivo.toPath(),
                stringHTML,
                StandardCharsets.UTF_8);
    }

    private String generarHTML() {

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="es">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Reporte de balances financieros</title>
                """);

        agregarEstilosCSS(html);

        html.append("""
                </head>

                <body>

                    <h1>Reporte de balances financieros</h1>

                    <table>

                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Fecha y hora</th>
                                <th>Ingresos</th>
                                <th>Egresos</th>
                                <th>Balance</th>
                                <th>Resultado</th>
                            </tr>
                        </thead>

                        <tbody>
                """);

        agregarBalances(html);

        html.append("""
                        </tbody>

                    </table>

                </body>

                </html>
                """);

        return html.toString();
    }

    private void agregarBalances(StringBuilder html) {

        if (balances == null || balances.isEmpty()) {

            html.append("""
                    <tr>
                        <td colspan="6">
                            No hay balances financieros registrados.
                        </td>
                    </tr>
                    """);

            return;
        }

        for (BalanceFinanciero balance : balances) {

            String descripcion = obtenerDescripcionBalance(balance.getBalance());

            html.append("<tr>");

            html.append("<td>")
                    .append(balance.getId())
                    .append("</td>");

            html.append("<td>")
                    .append(balance.getFechaHora())
                    .append("</td>");

            html.append("<td>Q. ")
                    .append(balance.getMontoIngresos())
                    .append("</td>");

            html.append("<td>Q. ")
                    .append(balance.getMontoEgresos())
                    .append("</td>");

            html.append("<td>Q. ")
                    .append(balance.getBalance())
                    .append("</td>");

            html.append("<td>")
                    .append(descripcion)
                    .append("</td>");

            html.append("</tr>");
        }
    }

    private String obtenerDescripcionBalance(BigDecimal balance) {

        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            return "PÉRDIDA";
        }

        if (balance.compareTo(BigDecimal.ZERO) > 0) {
            return "GANANCIA";
        }

        return "BALANCE NEUTRO";
    }

    private void agregarEstilosCSS(StringBuilder html) {

        html.append("""
                <style>

                    body {
                        margin: 0;
                        padding: 30px;
                        background-color: #323423;
                        color: white;
                        font-family: Arial, Helvetica, sans-serif;
                    }

                    h1 {
                        color: #e38758;
                        text-align: center;
                        margin-bottom: 30px;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                    }

                    th {
                        color: white;
                        font-weight: bold;
                        border: 1px solid white;
                        padding: 10px;
                    }

                    td {
                        color: white;
                        border: 1px solid white;
                        padding: 10px;
                        text-align: center;
                    }

                </style>
                """);
    }
}
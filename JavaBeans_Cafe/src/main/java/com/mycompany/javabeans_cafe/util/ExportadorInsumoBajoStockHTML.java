package com.mycompany.javabeans_cafe.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.InsumoDAO;
import com.mycompany.javabeans_cafe.modelos.Insumo;

public class ExportadorInsumoBajoStockHTML {
    private List<Insumo> insumosBajoStock;

    public void exportar(File archivo) throws IOException, SQLException {
        InsumoDAO insumoDAO = new InsumoDAO();
        insumosBajoStock = insumoDAO.obtenerConStockBajo();

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

                    <title>Insumos con bajo stock</title>
                """);

        agregarEstilosCSS(html);

        html.append("""
                </head>

                <body>

                    <h1>Insumos con bajo stock</h1>

                    <table>

                        <thead>
                            <tr>
                                <th>Código insumo</th>
                                <th>Nombre</th>
                                <th>Stock actual</th>
                                <th>Stock mínimo</th>
                            </tr>
                        </thead>

                        <tbody>
                """);

        agregarInsumos(html);

        html.append("""
                        </tbody>

                    </table>

                </body>

                </html>
                """);

        return html.toString();
    }

    private void agregarInsumos(StringBuilder html) {

        if (insumosBajoStock == null
                || insumosBajoStock.isEmpty()) {

            html.append("""
                    <tr>
                        <td colspan="4">
                            No hay insumos con bajo stock.
                        </td>
                    </tr>
                    """);

            return;
        }

        for (Insumo insumo : insumosBajoStock) {

            html.append("<tr>");

            html.append("<td>")
                    .append(insumo.getCodigoInsumo())
                    .append("</td>");

            html.append("<td>")
                    .append(insumo.getNombreInsumo())
                    .append("</td>");

            html.append("<td>")
                    .append(insumo.getStockActual())
                    .append("</td>");

            html.append("<td>")
                    .append(insumo.getStockMinimo())
                    .append("</td>");

            html.append("</tr>");
        }
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

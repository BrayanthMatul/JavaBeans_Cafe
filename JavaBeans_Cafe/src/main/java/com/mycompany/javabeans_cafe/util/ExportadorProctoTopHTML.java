package com.mycompany.javabeans_cafe.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.ProductoMasVendidoDAO;
import com.mycompany.javabeans_cafe.modelos.ProductoMasVendido;

public class ExportadorProctoTopHTML {

    private List<ProductoMasVendido> productosMasVendidos;

    public void exportar(File archivo) throws IOException, SQLException {
        ProductoMasVendidoDAO productoMasVendidoDAO = new ProductoMasVendidoDAO();
        this.productosMasVendidos = productoMasVendidoDAO.obtenerProductosMasVendidos();

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

                    <title>Productos más vendidos</title>
                """);

        agregarEstilosCSS(html);

        html.append("""
                </head>

                <body>

                    <h1>Productos más vendidos</h1>

                    <table>

                        <thead>
                            <tr>
                                <th>Posición</th>
                                <th>Código producto</th>
                                <th>Nombre producto</th>
                                <th>Cantidad vendida</th>
                            </tr>
                        </thead>

                        <tbody>
                """);

        agregarProductos(html);

        html.append("""
                        </tbody>

                    </table>

                </body>

                </html>
                """);

        return html.toString();
    }

    private void agregarProductos(StringBuilder html) {

        if (productosMasVendidos == null
                || productosMasVendidos.isEmpty()) {

            html.append("""
                    <tr>
                        <td colspan="4">
                            No hay productos vendidos registrados.
                        </td>
                    </tr>
                    """);

            return;
        }

        int posicion = 1;

        for (ProductoMasVendido producto : productosMasVendidos) {

            html.append("<tr>");

            html.append("<td>")
                    .append(posicion)
                    .append("</td>");

            html.append("<td>")
                    .append(producto.getCodigoProducto())
                    .append("</td>");

            html.append("<td>")
                    .append(producto.getNombreProducto())
                    .append("</td>");

            html.append("<td>")
                    .append(producto.getCantidadVendida())
                    .append("</td>");

            html.append("</tr>");

            posicion++;
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

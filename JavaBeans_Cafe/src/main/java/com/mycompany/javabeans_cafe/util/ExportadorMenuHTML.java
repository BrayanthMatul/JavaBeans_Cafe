package com.mycompany.javabeans_cafe.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Base64;

import com.mycompany.javabeans_cafe.modelos.ProductoMenu;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author matul
 */
public class ExportadorMenuHTML {

    private List<ProductoMenu> bebidasCalientes;
    private List<ProductoMenu> bebidasFrias;
    private List<ProductoMenu> postres;
    private List<ProductoMenu> comidas;

    public ExportadorMenuHTML(List<ProductoMenu> bebidasCalientes, List<ProductoMenu> bebidasFrias,
            List<ProductoMenu> postres, List<ProductoMenu> comidas) {
        this.bebidasCalientes = bebidasCalientes;
        this.bebidasFrias = bebidasFrias;
        this.postres = postres;
        this.comidas = comidas;
    }

    public void exportar(File archivo) throws IOException {
        String stringHTML = generarHTML();
        Files.writeString(archivo.toPath(), stringHTML, StandardCharsets.UTF_8);
    }

    private String generarHTML() {

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="es">
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Menú JavaBeans Café</title>
                    """);

        agregarEstilosCSS(html);
        html.append("""
                </head>
                <body>
                <header>
                <h1>JavaBeans Café</h1>
                <h2>Menú</h2>
                </header>
                <main>
                    """);

        agregarCategoria(html, "Bebidas Calientes", bebidasCalientes);
        agregarCategoria(html, "Bebidas Frías", bebidasFrias);
        agregarCategoria(html, "Postres", postres);
        agregarCategoria(html, "Comidas", comidas);

        return html.toString();
    }

    private void agregarCategoria(StringBuilder html, String nombreCategoria, List<ProductoMenu> productos) {
        html.append("""
                <section class="categoria">
                """);

        html.append("<h2 class=\"titulo-categoria\">").append(nombreCategoria).append("</h2>");

        if (productos == null || productos.isEmpty()) {
            html.append("""
                    <p class="sin-productos">
                        No hay productos disponibles en esta categoría.
                    </p>
                    """);
        } else {
            html.append("<div class=\"productos\">");

            for (ProductoMenu producto : productos) {
                agregarProducto(html, producto);
            }

            html.append("</div>");
        }
        html.append("</section>");
    }

    private void agregarProducto(StringBuilder html, ProductoMenu producto) {
        html.append("<article class=\"producto\">");
        agregarImagen(html, producto);
        html.append("<div class=\"info-producto\">");
        html.append("<h3 class=\"nombre-producto\">").append(producto.getNombreProducto()).append("</h3>");
        html.append("<p class=\"precio\">Q. ").append(producto.getPrecioVenta()).append("</p>");
        html.append("</div>");
        html.append("</article>");
    }

    private void agregarImagen(StringBuilder html, ProductoMenu producto) {
        byte[] imagen = producto.getImagen();

        if (imagen == null || imagen.length == 0) {
            return;
        }

        String imagenBase64 = Base64.getEncoder().encodeToString(imagen);
        String tipoImagen = detectarTipoImagen(imagen);

        html.append("<img src=\"data:").append(tipoImagen).append(";base64,").append(imagenBase64)
                .append("\" alt=\"")
                .append(producto.getNombreProducto())
                .append("\">");
    }

    private String detectarTipoImagen(byte[] imagen) {

        if (imagen == null || imagen.length < 4) {
            return "image/jpeg";
        }

        if ((imagen[0] & 0xFF) == 0xFF && (imagen[1] & 0xFF) == 0xD8) {
            return "image/jpeg";
        }

        if ((imagen[0] & 0xFF) == 0x89 && imagen[1] == 0x50 && imagen[2] == 0x4E && imagen[3] == 0x47) {
            return "image/png";
        }

        if (imagen[0] == 'G' && imagen[1] == 'I' && imagen[2] == 'F') {
            return "image/gif";
        }

        return "image/jpeg";
    }

    private void agregarEstilosCSS(StringBuilder html) {
        html.append("""
                <style>

                * {
                    box-sizing: border-box;
                }

                body {
                    margin: 0;
                    padding: 0;
                    background-color: #323423;
                    color: white;
                    font-family: Arial, Helvetica, sans-serif;
                }

                header {
                    text-align: center;
                    padding: 35px 20px;
                    background-color: #27291b;
                }

                header h1 {
                    margin: 0;
                    color: #f29b52;
                    font-size: 38px;
                }

                header h2 {
                    margin: 0;
                    color: #ffffff;
                    font-size: 28px;
                    font-weight: bold;
                }

                header p {
                    margin-top: 8px;
                    color: #d6d6d6;
                }

                main {
                    width: 90%;
                    max-width: 1200px;
                    margin: auto;
                    padding: 20px 0 50px;
                }

                .categoria {
                    margin-top: 40px;
                }

                .titulo-categoria {
                    color: #f29b52;
                    font-size: 27px;
                    border-bottom: 2px solid #f29b52;
                    padding-bottom: 8px;
                    margin-bottom: 22px;
                }

                .productos {
                    display: grid;
                    grid-template-columns:
                        repeat(auto-fill, minmax(220px, 1fr));
                    gap: 20px;
                }

                .producto {
                    background-color: #40422e;
                    border-radius: 10px;
                    overflow: hidden;
                    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.30);
                    transition: transform 0.2s ease;
                }

                .producto img {
                    width: 100%;
                    height: 180px;
                    object-fit: cover;
                    display: block;
                }

                .info-producto {
                    padding: 15px;
                }

                .nombre-producto {
                    margin: 0;
                    color: white;
                    font-size: 20px;
                }

                .precio {
                    margin: 10px 0 0;
                    color: #f29b52;
                    font-size: 19px;
                    font-weight: bold;
                }

                .sin-productos {
                    color: #cccccc;
                    font-style: italic;
                }
                    </style>

                      """);
    }

}

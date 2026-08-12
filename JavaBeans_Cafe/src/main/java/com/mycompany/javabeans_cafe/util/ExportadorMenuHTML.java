package com.mycompany.javabeans_cafe.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

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

    public ExportadorMenuHTML(List<ProductoMenu> bebidasCalientes, List<ProductoMenu> bebidasFrias, List<ProductoMenu> postres, List<ProductoMenu> comidas) {
        this.bebidasCalientes = bebidasCalientes;
        this.bebidasFrias = bebidasFrias;
        this.postres = postres;
        this.comidas = comidas;
    }

    private String genrarHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Menú del Café</title></head><body>");
        html.append("<h1>Menú JavaBeans Cafe</h1>");

        html.append("<h2>Bebidas Calientes</h2>");
        html.append("<ul>");
        for (ProductoMenu producto : bebidasCalientes) {
            html.append("<li>").append(producto.getNombreProducto()).append(" - Q. ").append(producto.getPrecioVenta().toString()).append("</li>");
        }
        html.append("</ul>");

        html.append("<h2>Bebidas Frías</h2>");
        html.append("<ul>");
        for (ProductoMenu producto : bebidasFrias) {
            html.append("<li>").append(producto.getNombreProducto()).append(" - Q. ").append(producto.getPrecioVenta().toString()).append("</li>");
        }
        html.append("</ul>");

        html.append("<h2>Postres</h2>");
        html.append("<ul>");
        for (ProductoMenu producto : postres) {
            html.append("<li>").append(producto.getNombreProducto()).append(" - Q. ").append(producto.getPrecioVenta().toString()).append("</li>");
        }
        html.append("</ul>");

        html.append("<h2>Comidas</h2>");
        html.append("<ul>");
        for (ProductoMenu producto : comidas) {
            html.append("<li>").append(producto.getNombreProducto()).append(" - Q. ").append(producto.getPrecioVenta().toString()).append("</li>");
        }
        html.append("</ul>");

        html.append("</body></html>");

        return html.toString();
    }

    public void exportar(File archivo) throws IOException {
        String stringHTML = genrarHTML();
        Files.writeString(archivo.toPath(), stringHTML, StandardCharsets.UTF_8);
    }


    
}

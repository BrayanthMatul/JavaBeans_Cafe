/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javabeans_cafe.servicios;

import com.mycompany.javabeans_cafe.daos.CompraDAO;
import com.mycompany.javabeans_cafe.daos.InsumoDAO;
import com.mycompany.javabeans_cafe.db.ConexionBD;
import com.mycompany.javabeans_cafe.modelos.Compra;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author matul
 */
public class CompraInsumoServicio {
    
    public void registrarCompra(Compra compra) throws SQLException {
        CompraDAO compraDAO = new CompraDAO();
        InsumoDAO insumoDAO = new InsumoDAO();
        
        try (Connection conexion = ConexionBD.getConexion()) {
            conexion.setAutoCommit(false);
            try {
                compraDAO.insertarCompra( conexion, compra);
                insumoDAO.aumentarStock(conexion, compra.getCodigoInsumo(), compra.getCantidad());
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw new SQLException("Error al registrar la compra: " + e.getMessage());
            }
        }
    }
}
    

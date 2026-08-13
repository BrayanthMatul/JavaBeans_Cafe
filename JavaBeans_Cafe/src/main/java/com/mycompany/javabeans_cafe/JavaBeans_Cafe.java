/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.javabeans_cafe;

import com.mycompany.javabeans_cafe.interfaces_graficas.LoginFrame;
import com.mycompany.javabeans_cafe.servicios.PagoPendienteAutomaticoServicio;
import java.sql.SQLException;

/**
 *
 * @author matul
 */
public class JavaBeans_Cafe {

    public static void main(String[] args) {
        ejecutarPagosPendientes();
        LoginFrame loginFrame = new LoginFrame();
        loginFrame.setVisible(true);
    }
    
    private static void ejecutarPagosPendientes(){
        try {
        PagoPendienteAutomaticoServicio pagoAutomatico = new PagoPendienteAutomaticoServicio();
        pagoAutomatico.ejecutarPagoAutomatico();
            
        } catch (SQLException e) {
            System.out.println("Error al generar pagos automáticos: " + e.getMessage()
            );
        }
    }
}

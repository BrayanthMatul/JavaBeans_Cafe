/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestionn_insumos;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.mycompany.javabeans_cafe.daos.InsumoDAO;
import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import com.mycompany.javabeans_cafe.interfaces_graficas.modales.MensajeDialogFrame;
import com.mycompany.javabeans_cafe.modelos.Insumo;
import com.mycompany.javabeans_cafe.util.RecolectorDeDatos;
import com.mycompany.javabeans_cafe.util.VerificadorInsumo;

/**
 *
 * @author matul
 */
public class InternalRegistrarInsumo extends javax.swing.JInternalFrame {

    private RecolectorDeDatos recolector;
    private boolean errorEnRecolector;
    private boolean errorEnValidacion;
    private String nombreInsumo;
    private String nombreUnidadMedida;
    private int stockMinimo;
    private BigDecimal costoInsumo;

    /**
     * Creates new form InternalRegistrarInsumo
     */
    public InternalRegistrarInsumo() {
        initComponents();

        this.errorEnRecolector = false;
        this.errorEnValidacion = false;

    }

    private void recolectarDatosInsumo() {
        this.errorEnRecolector = false;
        recolector = new RecolectorDeDatos();
        String mensajeError = "";

        try {
            this.nombreInsumo = recolector.recolectarTexto(jTextFieldNombreInsumo);
        } catch (TextoVacioException e) {
            mensajeError = "El campo nombre del insumo no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.nombreUnidadMedida = recolector.recolectarTexto(jTextFieldUnidadMedida);
        } catch (TextoVacioException e) {
            mensajeError = "El campo unidad de medida no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.stockMinimo = recolector.recolectarEntero(jTextFieldStockMinimo);
        } catch (TextoVacioException e) {
            mensajeError = "El campo stock minimo no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo stock minimo contiene un número inválido.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.costoInsumo = recolector.recolectarBigDecimals(jTextFieldCostoInsumo);
        } catch (TextoVacioException e) {
            mensajeError = "El campo costo del insumo no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo costo del insumo contiene un número inválido.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }
    }

    private void validarDatosInsumo() {
        errorEnValidacion = false;
        String mensajeError = "";

        VerificadorInsumo verificador = new VerificadorInsumo();

        try {

            if (verificador.existeNombreInsumo(nombreInsumo)) {
                mensajeError = "Ya existe un insumo con ese nombre, por favor elija otro";
                mostrarMensajeErrorValidador(mensajeError);
            }
        } catch (SQLException e) {
            mensajeError = "Error al verificar los datos en la base de datos: " + e.getMessage();
            mostrarMensajeErrorValidador(mensajeError);
        }
    }

    private void mostrarMensajeErrorRecolector(String mensaje) {
        MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeErrorFrame.setVisible(true);
        errorEnRecolector = true;
    }

    private void mostrarMensajeErrorValidador(String mensaje) {
        MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeErrorFrame.setVisible(true);
        errorEnValidacion = true;
    }

    private void guardarInsumo() {
        int stockActual = 0; // Se asume que no hay stock
        Insumo nuevoInsumo = new Insumo(nombreInsumo, nombreUnidadMedida, stockActual, stockMinimo, costoInsumo);
        InsumoDAO registradorInsumo = new InsumoDAO();

        try {
            registradorInsumo.insertarInsumo(nuevoInsumo);
            String mensaje = "Insumo creado exitosamente.";
            MensajeDialogFrame mensajeExitoFrame = new MensajeDialogFrame(null, true, mensaje, false);
            mensajeExitoFrame.setVisible(true);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            String mensaje = "Error al crear el insumo: " + e.getMessage();
            MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
            mensajeErrorFrame.setVisible(true);
        }
    }

    private void limpiarCampos() {
        jTextFieldNombreInsumo.setText("");
        jTextFieldUnidadMedida.setText("");
        jTextFieldStockMinimo.setText("");
        jTextFieldCostoInsumo.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jPanelCentrado = new javax.swing.JPanel();
        jPanelFormulario = new javax.swing.JPanel();
        jPanelNombreInsumo = new javax.swing.JPanel();
        jLabelNombreInsumo = new javax.swing.JLabel();
        jTextFieldNombreInsumo = new javax.swing.JTextField();
        jPanelUnidadMedida = new javax.swing.JPanel();
        jLabelUnidadDeMedida = new javax.swing.JLabel();
        jTextFieldUnidadMedida = new javax.swing.JTextField();
        jPanelStockMinimo = new javax.swing.JPanel();
        jLabelStockMinimo = new javax.swing.JLabel();
        jTextFieldStockMinimo = new javax.swing.JTextField();
        jPanelCostoInsumo = new javax.swing.JPanel();
        jLabelCostoInsumo = new javax.swing.JLabel();
        jTextFieldCostoInsumo = new javax.swing.JTextField();
        jPanelBoton = new javax.swing.JPanel();
        jButtonRealizar = new javax.swing.JButton();

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Registrar Nuevo Insumo");
        jLabelTitulo.setOpaque(true);
        jPanelTitulo.add(jLabelTitulo);

        getContentPane().add(jPanelTitulo, java.awt.BorderLayout.NORTH);

        jPanelCentrado.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentrado.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario.setLayout(new javax.swing.BoxLayout(jPanelFormulario, javax.swing.BoxLayout.Y_AXIS));

        jPanelNombreInsumo.setBackground(new java.awt.Color(50, 52, 35));
        jPanelNombreInsumo.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelNombreInsumo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelNombreInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNombreInsumo.setText("Nombre Insumo");
        jLabelNombreInsumo.setOpaque(true);
        jPanelNombreInsumo.add(jLabelNombreInsumo);

        jTextFieldNombreInsumo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jTextFieldNombreInsumo.setDisabledTextColor(new java.awt.Color(50, 52, 35));
        jPanelNombreInsumo.add(jTextFieldNombreInsumo);

        jPanelFormulario.add(jPanelNombreInsumo);

        jPanelUnidadMedida.setBackground(new java.awt.Color(50, 52, 35));
        jPanelUnidadMedida.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelUnidadDeMedida.setBackground(new java.awt.Color(50, 52, 35));
        jLabelUnidadDeMedida.setForeground(new java.awt.Color(255, 255, 255));
        jLabelUnidadDeMedida.setText("Unidad de medida");
        jLabelUnidadDeMedida.setOpaque(true);
        jPanelUnidadMedida.add(jLabelUnidadDeMedida);

        jTextFieldUnidadMedida.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelUnidadMedida.add(jTextFieldUnidadMedida);

        jPanelFormulario.add(jPanelUnidadMedida);

        jPanelStockMinimo.setBackground(new java.awt.Color(50, 52, 35));
        jPanelStockMinimo.setLayout(new java.awt.GridLayout(0, 1, 2, 5));

        jLabelStockMinimo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelStockMinimo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelStockMinimo.setText("Stock minimo (Numero entero)");
        jLabelStockMinimo.setOpaque(true);
        jPanelStockMinimo.add(jLabelStockMinimo);

        jTextFieldStockMinimo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelStockMinimo.add(jTextFieldStockMinimo);

        jPanelFormulario.add(jPanelStockMinimo);

        jPanelCostoInsumo.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCostoInsumo.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelCostoInsumo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelCostoInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCostoInsumo.setText("Costo Insumo (en Q.)");
        jLabelCostoInsumo.setOpaque(true);
        jPanelCostoInsumo.add(jLabelCostoInsumo);

        jTextFieldCostoInsumo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelCostoInsumo.add(jTextFieldCostoInsumo);

        jPanelFormulario.add(jPanelCostoInsumo);

        jPanelCentrado.add(jPanelFormulario, new java.awt.GridBagConstraints());

        getContentPane().add(jPanelCentrado, java.awt.BorderLayout.CENTER);

        jPanelBoton.setBackground(new java.awt.Color(50, 52, 35));

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Registrar Insumo");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanelBoton.add(jButtonRealizar);

        getContentPane().add(jPanelBoton, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRealizarActionPerformed
        recolectarDatosInsumo();

        if (errorEnRecolector) {
            return;
        }

        validarDatosInsumo();

        if (errorEnValidacion) {
            return;
        }

        guardarInsumo();
    }//GEN-LAST:event_jButtonRealizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JLabel jLabelCostoInsumo;
    private javax.swing.JLabel jLabelNombreInsumo;
    private javax.swing.JLabel jLabelStockMinimo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUnidadDeMedida;
    private javax.swing.JPanel jPanelBoton;
    private javax.swing.JPanel jPanelCentrado;
    private javax.swing.JPanel jPanelCostoInsumo;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelNombreInsumo;
    private javax.swing.JPanel jPanelStockMinimo;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JPanel jPanelUnidadMedida;
    private javax.swing.JTextField jTextFieldCostoInsumo;
    private javax.swing.JTextField jTextFieldNombreInsumo;
    private javax.swing.JTextField jTextFieldStockMinimo;
    private javax.swing.JTextField jTextFieldUnidadMedida;
    // End of variables declaration//GEN-END:variables
}

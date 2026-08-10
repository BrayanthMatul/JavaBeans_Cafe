/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestionn_insumos;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.InsumoDAO;
import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import com.mycompany.javabeans_cafe.interfaces_graficas.modales.MensajeDialogFrame;
import com.mycompany.javabeans_cafe.modelos.Compra;
import com.mycompany.javabeans_cafe.modelos.Insumo;
import com.mycompany.javabeans_cafe.servicios.CompraInsumoServicio;
import com.mycompany.javabeans_cafe.util.RecolectorDeDatos;

/**
 *
 * @author matul
 */
public class InternalRegistrarCompraInsumo extends javax.swing.JInternalFrame {

    private RecolectorDeDatos recolector;
    private boolean errorEnRecolector;
    private int codigoInsumo;
    private int cantidad;

    private Timestamp fechaCompra;
    private BigDecimal monto;
    private boolean contabilizado;

    private Insumo insumoSeleccionado;
    private Compra compraNueva;
    private List<Insumo> insumosCargados = new ArrayList<>();

    /**
     * Creates new form InternalRegistrarCompraInsumo
     */
    public InternalRegistrarCompraInsumo() {
        initComponents();

        this.errorEnRecolector = false;
        cargarInsumos();

        if (insumosCargados.isEmpty()) {
            String mensaje = "No hay insumos disponibles para registrar una compra.";
            MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
            mensajeErrorFrame.setVisible(true);
            jButtonRealizar.setEnabled(false);
            jButtonRealizar.setToolTipText("No hay insumos disponibles para registrar una compra.");
        }
    }

    private void cargarInsumos() {

        jComboBoxInsumos.removeAllItems();
        insumosCargados.clear();

        InsumoDAO insumoDAO = new InsumoDAO();

        try {
            List<Insumo> insumos = insumoDAO.obtenerTodos();
            insumosCargados.addAll(insumos);
            for (Insumo insumo : insumos) {
                jComboBoxInsumos.addItem(insumo.getNombreInsumo());
            }
        } catch (SQLException e) {
            String mensajeError = "Error al cargar insumos: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
    }

    private void recolectarDatosInsumo() {
        this.errorEnRecolector = false;
        recolector = new RecolectorDeDatos();
        String mensajeError = "";

        obtenerInsumoSeleccionado();

        try {
            this.cantidad = recolector.recolectarEntero(jTextFieldCantidad);

            if (cantidad <= 0) {
                mensajeError = "La cantidad comprada debe ser mayor que cero.";
                mostrarMensajeErrorRecolector(mensajeError);
                return;
            }
        } catch (TextoVacioException e) {
            mensajeError = "El campo cantidad no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo cantidad debe ser un número entero válido.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }
    }

    private void mostrarMensajeErrorRecolector(String mensaje) {
        MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeErrorFrame.setVisible(true);
        errorEnRecolector = true;
    }

    private void obtenerInsumoSeleccionado() {
        int indiceSeleccionado = jComboBoxInsumos.getSelectedIndex();
        this.insumoSeleccionado = insumosCargados.get(indiceSeleccionado);
    }

    private void completarDatosCompra() {
        this.codigoInsumo = insumoSeleccionado.getCodigoInsumo();
        this.fechaCompra = new Timestamp(System.currentTimeMillis());
        this.monto = insumoSeleccionado.getCostoInsumo().multiply(BigDecimal.valueOf(cantidad));
        this.contabilizado = false;

        this.compraNueva = new Compra(codigoInsumo, fechaCompra, cantidad, monto, contabilizado);
    }

    private void guardarCompraInsumo() {
        CompraInsumoServicio compraInsumoServicio = new CompraInsumoServicio();
        try {
            compraInsumoServicio.registrarCompra(compraNueva);
            String mensaje = "Compra registrada exitosamente.";
            MensajeDialogFrame mensajeExitoFrame = new MensajeDialogFrame(null, true, mensaje, false);
            mensajeExitoFrame.setVisible(true);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            String mensaje = "Error al registrar la compra: " + e.getMessage();
            MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
            mensajeErrorFrame.setVisible(true);
        }
    }

    private void limpiarCampos() {
        jTextFieldCantidad.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTituloRegistroCompra = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jPanelBotonCompra = new javax.swing.JPanel();
        jButtonRealizar = new javax.swing.JButton();
        jPanelCentradoRegistro = new javax.swing.JPanel();
        jPanelFormulario1 = new javax.swing.JPanel();
        jPanelNombreInsumo1 = new javax.swing.JPanel();
        jLabelElijaInsumo = new javax.swing.JLabel();
        jComboBoxInsumos = new javax.swing.JComboBox<>();
        jPanelUnidadMedida1 = new javax.swing.JPanel();
        jLabelCantidad = new javax.swing.JLabel();
        jTextFieldCantidad = new javax.swing.JTextField();

        jPanelTituloRegistroCompra.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Registrar Compra");
        jLabelTitulo.setOpaque(true);
        jPanelTituloRegistroCompra.add(jLabelTitulo);

        getContentPane().add(jPanelTituloRegistroCompra, java.awt.BorderLayout.NORTH);

        jPanelBotonCompra.setBackground(new java.awt.Color(50, 52, 35));

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Registrar");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanelBotonCompra.add(jButtonRealizar);

        getContentPane().add(jPanelBotonCompra, java.awt.BorderLayout.SOUTH);

        jPanelCentradoRegistro.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentradoRegistro.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentradoRegistro.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentradoRegistro.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario1.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario1.setLayout(new javax.swing.BoxLayout(jPanelFormulario1, javax.swing.BoxLayout.Y_AXIS));

        jPanelNombreInsumo1.setBackground(new java.awt.Color(50, 52, 35));
        jPanelNombreInsumo1.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelElijaInsumo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelElijaInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelElijaInsumo.setText("Elija un insumo");
        jLabelElijaInsumo.setOpaque(true);
        jPanelNombreInsumo1.add(jLabelElijaInsumo);

        jComboBoxInsumos.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxInsumos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelNombreInsumo1.add(jComboBoxInsumos);

        jPanelFormulario1.add(jPanelNombreInsumo1);

        jPanelUnidadMedida1.setBackground(new java.awt.Color(50, 52, 35));
        jPanelUnidadMedida1.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelCantidad.setBackground(new java.awt.Color(50, 52, 35));
        jLabelCantidad.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCantidad.setText("Cantidad (unidades compradas)");
        jLabelCantidad.setOpaque(true);
        jPanelUnidadMedida1.add(jLabelCantidad);

        jTextFieldCantidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelUnidadMedida1.add(jTextFieldCantidad);

        jPanelFormulario1.add(jPanelUnidadMedida1);

        jPanelCentradoRegistro.add(jPanelFormulario1, new java.awt.GridBagConstraints());

        getContentPane().add(jPanelCentradoRegistro, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRealizar1ActionPerformed
        recolectarDatosInsumo();
        if (errorEnRecolector) {
            return;
        }

        completarDatosCompra();
        guardarCompraInsumo();
    }// GEN-LAST:event_jButtonRealizar1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JComboBox<String> jComboBoxInsumos;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelElijaInsumo;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelBotonCompra;
    private javax.swing.JPanel jPanelCentradoRegistro;
    private javax.swing.JPanel jPanelFormulario1;
    private javax.swing.JPanel jPanelNombreInsumo1;
    private javax.swing.JPanel jPanelTituloRegistroCompra;
    private javax.swing.JPanel jPanelUnidadMedida1;
    private javax.swing.JTextField jTextFieldCantidad;
    // End of variables declaration//GEN-END:variables
}

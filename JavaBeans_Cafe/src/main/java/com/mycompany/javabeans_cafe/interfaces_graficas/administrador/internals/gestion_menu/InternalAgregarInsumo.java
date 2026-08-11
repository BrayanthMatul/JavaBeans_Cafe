/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestion_menu;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.javabeans_cafe.daos.InsumoDAO;
import com.mycompany.javabeans_cafe.daos.InsumoProductoDAO;
import com.mycompany.javabeans_cafe.daos.ProductoMenuDAO;
import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import com.mycompany.javabeans_cafe.interfaces_graficas.modales.MensajeDialogFrame;
import com.mycompany.javabeans_cafe.modelos.Insumo;
import com.mycompany.javabeans_cafe.modelos.InsumoProducto;
import com.mycompany.javabeans_cafe.modelos.ProductoMenu;
import com.mycompany.javabeans_cafe.util.RecolectorDeDatos;

/**
 *
 * @author matul
 */
public class InternalAgregarInsumo extends javax.swing.JInternalFrame {

    private List<ProductoMenu> productosCargados = new ArrayList<>();
    private List<Insumo> insumosCargados = new ArrayList<>();
    private RecolectorDeDatos recolector;
    private boolean errorEnRecolector;
    private boolean errorEnValidacion;
    private BigDecimal cantidad;

    private ProductoMenu productoSeleccionado;
    private Insumo insumoSeleccionado;
    private boolean insumoYaAgregado;

    /**
     * Creates new form InternalAgregarInsumo
     */
    public InternalAgregarInsumo() {
        initComponents();
        cargarProductos();
        cargarInsumos();
        cargarCantidadDeInsumoPorProducto();
    }

    private void cargarProductos() {
        jComboBoxProductos.removeAllItems();
        productosCargados.clear();

        ProductoMenuDAO productoMenuDAO = new ProductoMenuDAO();

        try {
            List<ProductoMenu> productos = productoMenuDAO.obtenerTodos();
            productosCargados.addAll(productos);
            for (ProductoMenu producto : productos) {
                jComboBoxProductos.addItem(producto.getNombreProducto());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensajeError = "Error al cargar productos: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
    }

    private void cargarInsumos() {
        jComboBoxInsumo.removeAllItems();
        insumosCargados.clear();

        InsumoDAO insumoDAO = new InsumoDAO();

        try {
            List<Insumo> insumos = insumoDAO.obtenerTodos();
            insumosCargados.addAll(insumos);
            for (Insumo insumo : insumos) {
                jComboBoxInsumo.addItem(insumo.getNombreInsumo() + " (" + insumo.getUnidadMedida() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String mensajeError = "Error al cargar insumos: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
    }

    private void cargarCantidadDeInsumoPorProducto() {
        int indiceProducto = jComboBoxProductos.getSelectedIndex();
        int indiceInsumo = jComboBoxInsumo.getSelectedIndex();

        if (indiceProducto < 0 || indiceInsumo < 0 || indiceProducto >= productosCargados.size()
                || indiceInsumo >= insumosCargados.size()) {

            productoSeleccionado = null;
            insumoSeleccionado = null;
            jTextFieldCantidad.setText("");

            this.insumoYaAgregado = false;
            jTextFieldCantidad.setText("");
            return;
        }

        this.productoSeleccionado = productosCargados.get(jComboBoxProductos.getSelectedIndex());
        this.insumoSeleccionado = insumosCargados.get(jComboBoxInsumo.getSelectedIndex());

        InsumoProductoDAO insumoProductoDAO = new InsumoProductoDAO();
        try {
            BigDecimal cantidadInsumo = insumoProductoDAO.obtenerCantidadInsumoPorProducto(
                    productoSeleccionado.getCodigoProducto(),
                    insumoSeleccionado.getCodigoInsumo());
            if (cantidadInsumo != null) {
                this.insumoYaAgregado = true;
                jTextFieldCantidad.setText(String.valueOf(cantidadInsumo));
                jButtonRealizar.setText("Actualizar");
            } else {
                this.insumoYaAgregado = false;
                jTextFieldCantidad.setText("");
                jButtonRealizar.setText("Agregar");
            }
        } catch (SQLException e) {
            String mensajeError = "Error al cargar la cantidad de insumo por producto: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
    }

    private void recolectarDatosInsumoProducto() {
        this.errorEnRecolector = false;
        recolector = new RecolectorDeDatos();
        String mensajeError = "";

        this.productoSeleccionado = productosCargados.get(jComboBoxProductos.getSelectedIndex());
        this.insumoSeleccionado = insumosCargados.get(jComboBoxInsumo.getSelectedIndex());
        this.cantidad = BigDecimal.ZERO;

        try {
            this.cantidad = recolector.recolectarBigDecimals(jTextFieldCantidad);
        } catch (TextoVacioException e) {
            mensajeError = "El campo cantidad no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo cantidad contiene un número inválido.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

    }

    private void validarDatosInsumoProducto() {
        errorEnValidacion = false;
        String mensajeError = "";

        if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            mensajeError = "La cantidad debe ser mayor a cero.";
            mostrarMensajeErrorValidador(mensajeError);
        }
    }

    private void limpiarCampos() {
        jTextFieldCantidad.setText("");
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

    private void guardarProductoInsumo() {

        if (productoSeleccionado == null) {
            String mensajeError = "Por favor seleccione un producto antes de editar.";
            mostrarMensajeErrorValidador(mensajeError);
            return;
        }

        if (insumoSeleccionado == null) {
            String mensajeError = "Por favor seleccione un insumo antes de editar.";
            mostrarMensajeErrorValidador(mensajeError);
            return;
        }

        recolectarDatosInsumoProducto();
        if (errorEnRecolector) {
            return;
        }

        validarDatosInsumoProducto();

        if (errorEnValidacion) {
            return;
        }

        int codigoProducto = productoSeleccionado.getCodigoProducto();
        int codigoInsumo = insumoSeleccionado.getCodigoInsumo();
        InsumoProductoDAO insumoProductoDAO = new InsumoProductoDAO();

        try {
            String mensajeExito;

            if (insumoYaAgregado) {
                insumoProductoDAO.actualizarCantidad(codigoProducto, codigoInsumo, cantidad);
                mensajeExito = "Cantidad del insumo actualizada exitosamente.";
            } else {
                InsumoProducto nuevoInsumoProducto = new InsumoProducto(codigoInsumo, codigoProducto, cantidad);
                insumoProductoDAO.insertar(nuevoInsumoProducto);
                mensajeExito = "Insumo agregado al producto exitosamente.";
            }
            productoSeleccionado = null;
            insumoSeleccionado = null;
            insumoYaAgregado = true;
            jButtonRealizar.setText("Actualizar");
            jTextFieldCantidad.setText(String.valueOf(cantidad));
            MensajeDialogFrame frameExito = new MensajeDialogFrame(null, true, mensajeExito, false);
            frameExito.setVisible(true);
        } catch (SQLException e) {
            String mensajeError = "Error al actualizar el producto: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
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

        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelNota = new javax.swing.JLabel();
        jPanelCentrado = new javax.swing.JPanel();
        jPanelFormulario = new javax.swing.JPanel();
        jPanelNombreProducto = new javax.swing.JPanel();
        jLabelProducto = new javax.swing.JLabel();
        jComboBoxProductos = new javax.swing.JComboBox<>();
        jPanelCategoria = new javax.swing.JPanel();
        jLabelInsumo = new javax.swing.JLabel();
        jComboBoxInsumo = new javax.swing.JComboBox<>();
        jPanelPrecioVenta = new javax.swing.JPanel();
        jLabelCantidad = new javax.swing.JLabel();
        jTextFieldCantidad = new javax.swing.JTextField();
        jPanelBoton = new javax.swing.JPanel();
        jButtonRealizar = new javax.swing.JButton();

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jPanelTitulo.setLayout(new java.awt.BorderLayout());

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Agregar Insumo a Producto");
        jLabelTitulo.setOpaque(true);
        jPanelTitulo.add(jLabelTitulo, java.awt.BorderLayout.NORTH);

        jLabelNota.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNota.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNota.setText(
                "Nota: Si elige un insumo agregado anteriormente, se cargara la cantidad asiganada para editarla");
        jPanelTitulo.add(jLabelNota, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanelTitulo, java.awt.BorderLayout.NORTH);

        jPanelCentrado.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentrado.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario.setLayout(new javax.swing.BoxLayout(jPanelFormulario, javax.swing.BoxLayout.Y_AXIS));

        jPanelNombreProducto.setBackground(new java.awt.Color(50, 52, 35));
        jPanelNombreProducto.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelProducto.setBackground(new java.awt.Color(50, 52, 35));
        jLabelProducto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelProducto.setText("Elija un producto");
        jLabelProducto.setOpaque(true);
        jPanelNombreProducto.add(jLabelProducto);

        jComboBoxProductos.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxProductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jComboBoxProductos.addActionListener(this::jComboBoxProductosActionPerformed);
        jPanelNombreProducto.add(jComboBoxProductos);

        jPanelFormulario.add(jPanelNombreProducto);

        jPanelCategoria.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCategoria.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelInsumo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelInsumo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelInsumo.setText("Elija un insumo");
        jLabelInsumo.setOpaque(true);
        jPanelCategoria.add(jLabelInsumo);

        jComboBoxInsumo.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxInsumo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jComboBoxInsumo.addActionListener(this::jComboBoxInsumoActionPerformed);
        jPanelCategoria.add(jComboBoxInsumo);

        jPanelFormulario.add(jPanelCategoria);

        jPanelPrecioVenta.setBackground(new java.awt.Color(50, 52, 35));
        jPanelPrecioVenta.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelCantidad.setBackground(new java.awt.Color(50, 52, 35));
        jLabelCantidad.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCantidad.setText("Cantidad");
        jLabelCantidad.setOpaque(true);
        jPanelPrecioVenta.add(jLabelCantidad);

        jTextFieldCantidad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelPrecioVenta.add(jTextFieldCantidad);

        jPanelFormulario.add(jPanelPrecioVenta);

        jPanelCentrado.add(jPanelFormulario, new java.awt.GridBagConstraints());

        getContentPane().add(jPanelCentrado, java.awt.BorderLayout.CENTER);

        jPanelBoton.setBackground(new java.awt.Color(50, 52, 35));

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Agregar");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanelBoton.add(jButtonRealizar);

        getContentPane().add(jPanelBoton, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxProductosActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBoxProductosActionPerformed
        cargarCantidadDeInsumoPorProducto();
    }// GEN-LAST:event_jComboBoxProductosActionPerformed

    private void jComboBoxInsumoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBoxInsumoActionPerformed
        cargarCantidadDeInsumoPorProducto();
    }// GEN-LAST:event_jComboBoxInsumoActionPerformed

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRealizarActionPerformed
        guardarProductoInsumo();
    }// GEN-LAST:event_jButtonRealizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JComboBox<String> jComboBoxInsumo;
    private javax.swing.JComboBox<String> jComboBoxProductos;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelInsumo;
    private javax.swing.JLabel jLabelNota;
    private javax.swing.JLabel jLabelProducto;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelBoton;
    private javax.swing.JPanel jPanelCategoria;
    private javax.swing.JPanel jPanelCentrado;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelNombreProducto;
    private javax.swing.JPanel jPanelPrecioVenta;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JTextField jTextFieldCantidad;
    // End of variables declaration//GEN-END:variables
}

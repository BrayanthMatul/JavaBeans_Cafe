/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestion_menu;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.mycompany.javabeans_cafe.daos.ProductoMenuDAO;
import com.mycompany.javabeans_cafe.enums.CategoriaProducto;
import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import com.mycompany.javabeans_cafe.interfaces_graficas.modales.MensajeDialogFrame;
import com.mycompany.javabeans_cafe.modelos.ProductoMenu;
import com.mycompany.javabeans_cafe.util.RecolectorDeDatos;
import com.mycompany.javabeans_cafe.util.VerificadorDatosProducto;

/**
 *
 * @author matul
 */
public class InternalRegistroProductoMenu extends javax.swing.JInternalFrame {

    private RecolectorDeDatos recolector;
    private boolean errorEnRecolector;
    private boolean errorEnValidacion;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private BigDecimal precioVenta;
    private byte[] imagenSeleccionada;

    /**
     * Creates new form InternalRegistroProductoMenu
     */
    public InternalRegistroProductoMenu() {
        initComponents();
        cargarCategorias();
    }

    private void seleccionarImagen() {
        JFileChooser selectorArchivo = new JFileChooser();

        FileNameExtensionFilter filtroImagenes = new FileNameExtensionFilter(
                "Imágenes JPG, JPEG y PNG",
                "jpg",
                "jpeg",
                "png");

        selectorArchivo.setFileFilter(filtroImagenes);
        selectorArchivo.setAcceptAllFileFilterUsed(false);

        int resultado = selectorArchivo.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = selectorArchivo.getSelectedFile();
            cargarImagen(archivoSeleccionado);
        }
    }

    private void cargarImagen(File archivoImagen) {
        try {
            long tamañoMaximo = 5L * 1024 * 1024;

            if (archivoImagen.length() > tamañoMaximo) {
                mostrarMensajeError("La imagen no puede superar los 5 MB");
                return;
            }

            BufferedImage imagenOriginal = ImageIO.read(archivoImagen);

            if (imagenOriginal == null) {
                mostrarMensajeError("El archivo seleccionado no es una imagen válida");
                return;
            }

            imagenSeleccionada = Files.readAllBytes(archivoImagen.toPath());

            Image imagenEscalada = imagenOriginal.getScaledInstance(140, 140, Image.SCALE_SMOOTH);

            jLabelVistaPrevia.setText("");
            jLabelVistaPrevia.setIcon(new ImageIcon(imagenEscalada));
        } catch (IOException e) {
            mostrarMensajeError(
                    "Error al cargar la imagen: " + e.getMessage());
        }
    }

    private void mostrarMensajeError(String mensaje) {
        MensajeDialogFrame mensajeDialogFrame = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeDialogFrame.setVisible(true);
    }

    private void cargarCategorias() {
        jComboBoxCategoria.removeAllItems();

        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            jComboBoxCategoria.addItem(categoria.name());
        }
    }

    private void recolectarDatosProducto() {
        this.errorEnRecolector = false;
        recolector = new RecolectorDeDatos();
        String mensajeError = "";

        this.categoria = convertirStringACategoria((String) jComboBoxCategoria.getSelectedItem());

        try {
            this.nombreProducto = recolector.recolectarTexto(jTextFieldNombreProducto);
        } catch (TextoVacioException e) {
            mensajeError = "El campo nombre del producto no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.precioVenta = recolector.recolectarBigDecimals(jTextFieldPrecioVenta);
        } catch (TextoVacioException e) {
            mensajeError = "El campo precio de venta no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo precio de venta contiene un número inválido.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

    }

    private void validarDatosProducto() {
        errorEnValidacion = false;
        String mensajeError = "";

        VerificadorDatosProducto verificador = new VerificadorDatosProducto();

        try {

            if (verificador.existeNombreProducto(nombreProducto)) {
                mensajeError = "Ya existe un producto con ese nombre, por favor elija otro";
                mostrarMensajeErrorValidador(mensajeError);
                return;
            }

            if (!verificador.bigDecimalMayorQueCero(precioVenta)) {
                mensajeError = "El precio de venta debe ser mayor que cero";
                mostrarMensajeErrorValidador(mensajeError);
                return;
            }
        } catch (SQLException e) {
            mensajeError = "Error al verificar los datos en la base de datos: " + e.getMessage();
            mostrarMensajeErrorValidador(mensajeError);
        }

        if (imagenSeleccionada == null) {
            mensajeError = "Debe seleccionar una imagen para el producto";
            mostrarMensajeErrorValidador(mensajeError);
        }
    }

    private CategoriaProducto convertirStringACategoria(String categoriaString) {
        return CategoriaProducto.valueOf(categoriaString);
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

    private void limpiarCampos() {
        jTextFieldNombreProducto.setText("");
        jTextFieldPrecioVenta.setText("");
        jComboBoxCategoria.setSelectedIndex(0);
        jLabelVistaPrevia.setIcon(null);
        jLabelVistaPrevia.setText("Imagen");
        imagenSeleccionada = null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jPanelCentrado = new javax.swing.JPanel();
        jPanelFormulario = new javax.swing.JPanel();
        jPanelNombreProducto = new javax.swing.JPanel();
        jLabelNombre = new javax.swing.JLabel();
        jTextFieldNombreProducto = new javax.swing.JTextField();
        jPanelPrecioVenta = new javax.swing.JPanel();
        jLabelPrecioVenta = new javax.swing.JLabel();
        jTextFieldPrecioVenta = new javax.swing.JTextField();
        jPanelCategoria = new javax.swing.JPanel();
        jLabelCategoria = new javax.swing.JLabel();
        jComboBoxCategoria = new javax.swing.JComboBox<>();
        jPanelSeleccionarImagen = new javax.swing.JPanel();
        jLabelImagen = new javax.swing.JLabel();
        jButtonImagen = new javax.swing.JButton();
        jPanelImagen = new javax.swing.JPanel();
        jLabelVistaPrevia = new javax.swing.JLabel();
        jPanelBoton = new javax.swing.JPanel();
        jButtonRealizar = new javax.swing.JButton();

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Registrar Nuevo Producto");
        jLabelTitulo.setOpaque(true);
        jPanelTitulo.add(jLabelTitulo);

        getContentPane().add(jPanelTitulo, java.awt.BorderLayout.NORTH);

        jPanelCentrado.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentrado.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario.setLayout(new javax.swing.BoxLayout(jPanelFormulario, javax.swing.BoxLayout.Y_AXIS));

        jPanelNombreProducto.setBackground(new java.awt.Color(50, 52, 35));
        jPanelNombreProducto.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelNombre.setBackground(new java.awt.Color(50, 52, 35));
        jLabelNombre.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNombre.setText("Nombre del producto");
        jLabelNombre.setOpaque(true);
        jPanelNombreProducto.add(jLabelNombre);

        jTextFieldNombreProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelNombreProducto.add(jTextFieldNombreProducto);

        jPanelFormulario.add(jPanelNombreProducto);

        jPanelPrecioVenta.setBackground(new java.awt.Color(50, 52, 35));
        jPanelPrecioVenta.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelPrecioVenta.setBackground(new java.awt.Color(50, 52, 35));
        jLabelPrecioVenta.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPrecioVenta.setText("Precio de Venta");
        jLabelPrecioVenta.setOpaque(true);
        jPanelPrecioVenta.add(jLabelPrecioVenta);

        jTextFieldPrecioVenta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelPrecioVenta.add(jTextFieldPrecioVenta);

        jPanelFormulario.add(jPanelPrecioVenta);

        jPanelCategoria.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCategoria.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelCategoria.setBackground(new java.awt.Color(50, 52, 35));
        jLabelCategoria.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCategoria.setText("Categoria");
        jLabelCategoria.setOpaque(true);
        jPanelCategoria.add(jLabelCategoria);

        jComboBoxCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxCategoria.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelCategoria.add(jComboBoxCategoria);

        jPanelFormulario.add(jPanelCategoria);

        jPanelSeleccionarImagen.setBackground(new java.awt.Color(50, 52, 35));
        jPanelSeleccionarImagen.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelImagen.setBackground(new java.awt.Color(50, 52, 35));
        jLabelImagen.setForeground(new java.awt.Color(255, 255, 255));
        jLabelImagen.setText("Imagen");
        jLabelImagen.setOpaque(true);
        jPanelSeleccionarImagen.add(jLabelImagen);

        jButtonImagen.setBackground(new java.awt.Color(50, 52, 35));
        jButtonImagen.setForeground(new java.awt.Color(255, 255, 255));
        jButtonImagen.setText("Seleccionar imagen");
        jButtonImagen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jButtonImagen.addActionListener(this::jButtonImagenActionPerformed);
        jPanelSeleccionarImagen.add(jButtonImagen);

        jPanelFormulario.add(jPanelSeleccionarImagen);

        jPanelImagen.setBackground(new java.awt.Color(50, 52, 35));
        jPanelImagen.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanelImagen.setLayout(new java.awt.BorderLayout());

        jLabelVistaPrevia.setBackground(new java.awt.Color(50, 52, 35));
        jLabelVistaPrevia.setForeground(new java.awt.Color(255, 255, 255));
        jLabelVistaPrevia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelVistaPrevia.setText("Imagen");
        jLabelVistaPrevia.setOpaque(true);
        jLabelVistaPrevia.setPreferredSize(new java.awt.Dimension(140, 140));
        jPanelImagen.add(jLabelVistaPrevia, java.awt.BorderLayout.CENTER);

        jPanelFormulario.add(jPanelImagen);

        jPanelCentrado.add(jPanelFormulario, new java.awt.GridBagConstraints());

        getContentPane().add(jPanelCentrado, java.awt.BorderLayout.CENTER);

        jPanelBoton.setBackground(new java.awt.Color(50, 52, 35));

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Registrar Producto");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanelBoton.add(jButtonRealizar);

        getContentPane().add(jPanelBoton, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarProductoMenu() {
        ProductoMenu nuevoProducto = new ProductoMenu(nombreProducto, categoria, precioVenta, imagenSeleccionada);
        ProductoMenuDAO registradorProducto = new ProductoMenuDAO();

        try {
            registradorProducto.insertarProducto(nuevoProducto);
            String mensaje = "Producto creado exitosamente.";
            MensajeDialogFrame mensajeExitoFrame = new MensajeDialogFrame(null, true, mensaje, false);
            mensajeExitoFrame.setVisible(true);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            String mensaje = "Error al crear el producto: " + e.getMessage();
            MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
            mensajeErrorFrame.setVisible(true);
        }
    }

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRealizarActionPerformed
        recolectarDatosProducto();

        if (errorEnRecolector) {
            return;
        }

        validarDatosProducto();

        if (errorEnValidacion) {
            return;
        }

        guardarProductoMenu();
    }// GEN-LAST:event_jButtonRealizarActionPerformed

    private void jButtonImagenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonImagenActionPerformed
        seleccionarImagen();
    }// GEN-LAST:event_jButtonImagenActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonImagen;
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JComboBox<String> jComboBoxCategoria;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPrecioVenta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelVistaPrevia;
    private javax.swing.JPanel jPanelBoton;
    private javax.swing.JPanel jPanelCategoria;
    private javax.swing.JPanel jPanelCentrado;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelImagen;
    private javax.swing.JPanel jPanelNombreProducto;
    private javax.swing.JPanel jPanelPrecioVenta;
    private javax.swing.JPanel jPanelSeleccionarImagen;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JTextField jTextFieldNombreProducto;
    private javax.swing.JTextField jTextFieldPrecioVenta;
    // End of variables declaration//GEN-END:variables
}

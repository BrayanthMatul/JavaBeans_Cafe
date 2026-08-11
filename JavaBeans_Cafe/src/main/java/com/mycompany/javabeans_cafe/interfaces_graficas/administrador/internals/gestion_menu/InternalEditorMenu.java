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
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
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
public class InternalEditorMenu extends javax.swing.JInternalFrame {

    private RecolectorDeDatos recolector;
    private boolean errorEnRecolector;
    private boolean errorEnValidacion;
    private String nombreProducto;
    private CategoriaProducto categoria;
    private BigDecimal precioVenta;
    private byte[] imagenSeleccionada;

    private List<ProductoMenu> productosCargados = new ArrayList<>();
    private ProductoMenu productoSeleccionado;

    /**
     * Creates new form InternalEditorMenu
     */
    public InternalEditorMenu() {
        initComponents();

        jSplitPane.setResizeWeight(0.3);

        SwingUtilities.invokeLater(() -> {
            jSplitPane.setDividerLocation(0.3);
        });

        cargarProductos();
        cargarCategorias();
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

    private void cargarDatosProductoSeleccionado() {
        jTextFieldNombreProducto.setText(productoSeleccionado.getNombreProducto());
        jTextFieldPrecioVenta.setText(productoSeleccionado.getPrecioVenta().toString());
        jComboBoxCategoria.setSelectedItem(productoSeleccionado.getCategoria().name());
        imagenSeleccionada = productoSeleccionado.getImagen();
        mostrarImagen(imagenSeleccionada);
    }

    private void mostrarImagen(byte[] imagen) {
        if (imagen == null || imagen.length == 0) {
            jLabelVistaPrevia.setIcon(null);
            jLabelVistaPrevia.setText("Sin imagen");
            return;
        }

        ImageIcon imagenOriginal = new ImageIcon(imagen);

        Image imagenEscalada = imagenOriginal.getImage()
                .getScaledInstance(
                        140,
                        140,
                        Image.SCALE_SMOOTH);

        jLabelVistaPrevia.setText("");
        jLabelVistaPrevia.setIcon(new ImageIcon(imagenEscalada));
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

            mostrarImagen(imagenSeleccionada);
        } catch (IOException e) {
            mostrarMensajeError(
                    "Error al cargar la imagen: " + e.getMessage());
        }
    }

    private void mostrarMensajeError(String mensaje) {
        MensajeDialogFrame mensajeDialogFrame = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeDialogFrame.setVisible(true);
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

            if (!productoSeleccionado.getNombreProducto().equalsIgnoreCase(nombreProducto)
                    && verificador.existeNombreProducto(nombreProducto)) {
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
            return;
        }

        if (imagenSeleccionada == null) {
            mensajeError = "Debe seleccionar una imagen para el producto";
            mostrarMensajeErrorValidador(mensajeError);
        }
    }

    private void limpiarCampos() {
        jTextFieldNombreProducto.setText("");
        jTextFieldPrecioVenta.setText("");
        jComboBoxCategoria.setSelectedIndex(-1);
        jLabelVistaPrevia.setIcon(null);
        jLabelVistaPrevia.setText("Imagen");
        imagenSeleccionada = null;
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

    private CategoriaProducto convertirStringACategoria(String categoriaString) {
        return CategoriaProducto.valueOf(categoriaString);
    }

    private void cargarCategorias() {
        jComboBoxCategoria.removeAllItems();

        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            jComboBoxCategoria.addItem(categoria.name());
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
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane = new javax.swing.JSplitPane();
        jPanelSeleccionador2 = new javax.swing.JPanel();
        jPanelTitulo3 = new javax.swing.JPanel();
        jLabelTituloSeleccionar2 = new javax.swing.JLabel();
        jPanelCentrado3 = new javax.swing.JPanel();
        jPanelFormulario3 = new javax.swing.JPanel();
        jPanelDPI15 = new javax.swing.JPanel();
        jLabelLista2 = new javax.swing.JLabel();
        jComboBoxProductos = new javax.swing.JComboBox<>();
        jPanelBoton3 = new javax.swing.JPanel();
        jButtonSeleccionar2 = new javax.swing.JButton();
        jPanelEditor = new javax.swing.JPanel();
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

        jPanelSeleccionador2.setLayout(new java.awt.BorderLayout());

        jPanelTitulo3.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTituloSeleccionar2.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloSeleccionar2.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTituloSeleccionar2.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTituloSeleccionar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloSeleccionar2.setText("Seleccionar Producto");
        jLabelTituloSeleccionar2.setOpaque(true);
        jPanelTitulo3.add(jLabelTituloSeleccionar2);

        jPanelSeleccionador2.add(jPanelTitulo3, java.awt.BorderLayout.NORTH);

        jPanelCentrado3.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentrado3.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentrado3.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentrado3.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario3.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario3.setLayout(new javax.swing.BoxLayout(jPanelFormulario3, javax.swing.BoxLayout.Y_AXIS));

        jPanelDPI15.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI15.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelLista2.setBackground(new java.awt.Color(50, 52, 35));
        jLabelLista2.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLista2.setText("Elija un producto para editar");
        jLabelLista2.setOpaque(true);
        jPanelDPI15.add(jLabelLista2);

        jComboBoxProductos.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxProductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI15.add(jComboBoxProductos);

        jPanelFormulario3.add(jPanelDPI15);

        jPanelCentrado3.add(jPanelFormulario3, new java.awt.GridBagConstraints());

        jPanelSeleccionador2.add(jPanelCentrado3, java.awt.BorderLayout.CENTER);

        jPanelBoton3.setBackground(new java.awt.Color(50, 52, 35));

        jButtonSeleccionar2.setBackground(new java.awt.Color(227, 135, 88));
        jButtonSeleccionar2.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonSeleccionar2.setText("Seleccionar");
        jButtonSeleccionar2.addActionListener(this::jButtonSeleccionar2ActionPerformed);
        jPanelBoton3.add(jButtonSeleccionar2);

        jPanelSeleccionador2.add(jPanelBoton3, java.awt.BorderLayout.SOUTH);

        jSplitPane.setLeftComponent(jPanelSeleccionador2);

        jPanelEditor.setLayout(new java.awt.BorderLayout());

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Editar Producto");
        jLabelTitulo.setOpaque(true);
        jPanelTitulo.add(jLabelTitulo);

        jPanelEditor.add(jPanelTitulo, java.awt.BorderLayout.NORTH);

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

        jTextFieldNombreProducto
                .setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
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

        jComboBoxCategoria.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
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

        jPanelEditor.add(jPanelCentrado, java.awt.BorderLayout.CENTER);

        jPanelBoton.setBackground(new java.awt.Color(50, 52, 35));

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Editar");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanelBoton.add(jButtonRealizar);

        jPanelEditor.add(jPanelBoton, java.awt.BorderLayout.SOUTH);

        jSplitPane.setRightComponent(jPanelEditor);

        getContentPane().add(jSplitPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSeleccionar2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSeleccionar2ActionPerformed
        int indiceSeleccionado = jComboBoxProductos.getSelectedIndex();

        if (indiceSeleccionado == -1) {
            return;
        }

        this.productoSeleccionado = productosCargados.get(indiceSeleccionado);

        cargarDatosProductoSeleccionado();
    }// GEN-LAST:event_jButtonSeleccionar2ActionPerformed

    private void jButtonImagenActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonImagenActionPerformed
        seleccionarImagen();
    }// GEN-LAST:event_jButtonImagenActionPerformed

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRealizarActionPerformed
        if (productoSeleccionado == null) {
            String mensajeError = "Por favor seleccione un producto antes de editar.";
            mostrarMensajeErrorValidador(mensajeError);
            return;
        }

        recolectarDatosProducto();
        if (errorEnRecolector) {
            return;
        }

        validarDatosProducto();

        if (errorEnValidacion) {
            return;
        }

        ProductoMenuDAO productoMenuDAO = new ProductoMenuDAO();

        ProductoMenu productoActualizado = new ProductoMenu(
                productoSeleccionado.getCodigoProducto(),
                nombreProducto,
                categoria,
                precioVenta,
                imagenSeleccionada);
        try {
            productoMenuDAO.actualizarProducto(productoActualizado);
            productoSeleccionado = null;
            cargarProductos();
            limpiarCampos();
            String mensajeExito = "Producto actualizado exitosamente.";
            MensajeDialogFrame frameExito = new MensajeDialogFrame(null, true, mensajeExito, false);
            frameExito.setVisible(true);
        } catch (SQLException e) {
            String mensajeError = "Error al actualizar el producto: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
    }// GEN-LAST:event_jButtonRealizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonImagen;
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JButton jButtonSeleccionar2;
    private javax.swing.JComboBox<String> jComboBoxCategoria;
    private javax.swing.JComboBox<String> jComboBoxProductos;
    private javax.swing.JLabel jLabelCategoria;
    private javax.swing.JLabel jLabelImagen;
    private javax.swing.JLabel jLabelLista2;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPrecioVenta;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelTituloSeleccionar2;
    private javax.swing.JLabel jLabelVistaPrevia;
    private javax.swing.JPanel jPanelBoton;
    private javax.swing.JPanel jPanelBoton3;
    private javax.swing.JPanel jPanelCategoria;
    private javax.swing.JPanel jPanelCentrado;
    private javax.swing.JPanel jPanelCentrado3;
    private javax.swing.JPanel jPanelDPI15;
    private javax.swing.JPanel jPanelEditor;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelFormulario3;
    private javax.swing.JPanel jPanelImagen;
    private javax.swing.JPanel jPanelNombreProducto;
    private javax.swing.JPanel jPanelPrecioVenta;
    private javax.swing.JPanel jPanelSeleccionador2;
    private javax.swing.JPanel jPanelSeleccionarImagen;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JPanel jPanelTitulo3;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JTextField jTextFieldNombreProducto;
    private javax.swing.JTextField jTextFieldPrecioVenta;
    // End of variables declaration//GEN-END:variables
}

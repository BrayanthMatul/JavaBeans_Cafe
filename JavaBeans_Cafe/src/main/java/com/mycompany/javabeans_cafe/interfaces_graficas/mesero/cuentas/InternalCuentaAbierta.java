/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.mesero.cuentas;

import com.mycompany.javabeans_cafe.daos.PedidoDAO;
import com.mycompany.javabeans_cafe.daos.ProductoMenuDAO;
import com.mycompany.javabeans_cafe.daos.ProductoPedidoDAO;
import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.StockInsuficienteException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import com.mycompany.javabeans_cafe.interfaces_graficas.modales.MensajeDialogFrame;
import com.mycompany.javabeans_cafe.modelos.Pedido;
import com.mycompany.javabeans_cafe.modelos.ProductoMenu;
import com.mycompany.javabeans_cafe.modelos.ProductoPedido;
import com.mycompany.javabeans_cafe.modelos.ProductoPedidoConNombre;
import com.mycompany.javabeans_cafe.servicios.CuentasServicio;
import com.mycompany.javabeans_cafe.util.RecolectorDeDatos;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author matul
 */
public class InternalCuentaAbierta extends javax.swing.JInternalFrame {

    private Pedido pedido;
    private CuentasServicio cuentasServicio;
    private List<ProductoMenu> productosCargados = new ArrayList<>();
    private RecolectorDeDatos recolector;
    private boolean errorEnRecolectorCantidadProducto;
    private boolean errorEnRecolectorCantidadPropina;
    private int cantidadProducto;
    private BigDecimal cantidadPropina;
    private ProductoMenu productoSeleccionado;
    private List<ProductoPedidoConNombre> productosPedidoSinConfirmar = new ArrayList<>();

    /**
     * Creates new form InternalCuentaAbierta
     */
    public InternalCuentaAbierta(int numeroMesa, int codigoEmpleado) {
        initComponents();

        jSplitPane.setResizeWeight(0.3);

        SwingUtilities.invokeLater(() -> {
            jSplitPane.setDividerLocation(0.3);
        });

        configurarNuevoPedido(numeroMesa, codigoEmpleado);
        cargarProductosDisponibles();
        cargarDatosPedido();
        cargarDatosTabla();

    }

    public InternalCuentaAbierta(int numeroMesa) {
        initComponents();

        jSplitPane.setResizeWeight(0.3);

        SwingUtilities.invokeLater(() -> {
            jSplitPane.setDividerLocation(0.3);
        });

        recuperarCuentaAbierta(numeroMesa);
        cargarProductosDisponibles();
        cargarDatosPedido();
        cargarDatosTabla();
    }

    public void eliminarProductoSinConfirmar(ProductoPedidoConNombre producto) {
        productosPedidoSinConfirmar = cuentasServicio.eliminarProductoSinConfirmar(producto,
                productosPedidoSinConfirmar);
        actualizarPanelesProductosSinConfirmar();
    }

    private void cargarProductosDisponibles() {
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

    private void configurarNuevoPedido(int numeroMesa, int codigoEmpleado) {
        try {
            this.cuentasServicio = new CuentasServicio(codigoEmpleado, numeroMesa);
            this.pedido = cuentasServicio.configurarNuevaCuenta();
        } catch (SQLException e) {
            String mensajeError = "Error al configurar el nuevo pedido: " + e.getMessage();
            mostrarMensajeError(mensajeError);
        }
    }

    private void recuperarCuentaAbierta(int numeroMesa) {
        try {
            this.cuentasServicio = new CuentasServicio(numeroMesa);
            this.pedido = cuentasServicio.recuperarCuentaAbierta();
        } catch (SQLException e) {
            String mensajeError = "Error al recuperar la cuenta abierta: " + e.getMessage();
            mostrarMensajeError(mensajeError);
        }
    }

    private void cargarDatosPedido() {
        jLabelTitulo1.setText("Cuenta Abierta No. " + pedido.getCodigoPedido());
        jLabelPropinaActual.setText(" Propina actual : Q. " + pedido.getPropina().toString());
        actualizarTotal();
    }

    private void actualizarTotal() {
        BigDecimal totalPedidos = pedido.getMontoPedido();
        BigDecimal totalConPropina = totalPedidos.add(pedido.getPropina());
        jLabelTotal.setText(" Total (Pedidos + Propina) : Q. " + totalPedidos.toString() + " + Q. "
                + pedido.getPropina().toString() + " = Q. " + totalConPropina.toString());
    }

    private void cargarDatosTabla() {
        DefaultTableModel modelo = (DefaultTableModel) jTableDetalles.getModel();
        modelo.setRowCount(0);

        modelo.setColumnIdentifiers(new Object[] { "Cantidad", "Producto", "Subtotal" });

        List<ProductoPedidoConNombre> productos = obtenerProductosPedido();

        for (ProductoPedidoConNombre productoConNombre : productos) {
            ProductoPedido producto = productoConNombre.getProductoPedido();
            Object[] fila = {
                    producto.getCantidad(),
                    productoConNombre.getNombreProducto(),
                    "Q. " + producto.getSubtotal()
            };

            modelo.addRow(fila);
        }
    }

    private List<ProductoPedidoConNombre> obtenerProductosPedido() {
        ProductoPedidoDAO productoPedidoDAO = new ProductoPedidoDAO();
        ProductoMenuDAO productoMenuDAO = new ProductoMenuDAO();
        List<ProductoPedidoConNombre> productosConNombre = new ArrayList<>();

        try {
            List<ProductoPedido> productos = productoPedidoDAO.obtenerPorPedido(pedido.getCodigoPedido());

            for (ProductoPedido producto : productos) {
                String nombreProducto = productoMenuDAO.obtenerNombreProductoPorCodigo(producto.getCodigoProducto());
                productosConNombre.add(new ProductoPedidoConNombre(producto, nombreProducto));
            }
        } catch (SQLException e) {
            String mensajeError = "Error al obtener los productos de la cuenta: " + e.getMessage();
            mostrarMensajeError(mensajeError);
        }

        return productosConNombre;
    }

    private void recolectarDatoProducto() {
        this.errorEnRecolectorCantidadProducto = false;
        recolector = new RecolectorDeDatos();

        this.productoSeleccionado = productosCargados.get(jComboBoxProductos.getSelectedIndex());

        if (productoSeleccionado == null) {
            String mensajeError = "Por favor seleccione un producto antes de editar.";
            mostrarMensajeError(mensajeError);
            return;
        }

        try {
            this.cantidadProducto = recolector.recolectarEntero(jTextFieldCantidadProducto);

            if (this.cantidadProducto <= 0) {
                mostrarMensajeErrorDatoProducto(
                        "El campo cantidad del producto debe ser un número entero mayor a cero.");
            }
        } catch (TextoVacioException e) {
            mostrarMensajeErrorDatoProducto("El campo cantidad del producto no puede estar vacio.");
        } catch (NumeroInvalidoException e) {
            mostrarMensajeErrorDatoProducto("El campo cantidad del producto debe ser un número entero válido.");
        }
        return;
    }

    private void actualizarListaProductosSinConfirmar() {
        recolectarDatoProducto();
        if (this.errorEnRecolectorCantidadProducto) {
            return;
        }

        ProductoPedido productoPedido = new ProductoPedido(
                productoSeleccionado.getCodigoProducto(),
                pedido.getCodigoPedido(),
                cantidadProducto,
                productoSeleccionado.getPrecioVenta().multiply(BigDecimal.valueOf(cantidadProducto)));

        ProductoPedidoConNombre productoPedidoConNombre = new ProductoPedidoConNombre(
                productoPedido, productoSeleccionado.getNombreProducto());

        productosPedidoSinConfirmar = cuentasServicio.agregarProductoSinConfirmar(productoPedidoConNombre,
                productosPedidoSinConfirmar);

        jTextFieldCantidadProducto.setText("");

    }

    private void actualizarPanelesProductosSinConfirmar() {
        jPanelListaSinConfirmar.removeAll();

        for (ProductoPedidoConNombre producto : productosPedidoSinConfirmar) {
            PanelProductoSinConfirmar panelProducto = new PanelProductoSinConfirmar(producto, this);
            jPanelListaSinConfirmar.add(panelProducto);
        }

        jPanelListaSinConfirmar.revalidate();
        jPanelListaSinConfirmar.repaint();
    }

    private void mostrarMensajeErrorDatoProducto(String mensaje) {
        this.errorEnRecolectorCantidadProducto = true;
        MensajeDialogFrame mensajeDialog = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeDialog.setVisible(true);
    }

    private void recolectarDatoPropina() {
        this.errorEnRecolectorCantidadPropina = false;
        recolector = new RecolectorDeDatos();

        try {
            this.cantidadPropina = recolector.recolectarBigDecimals(jTextFieldCantidadPropina);

            if (this.cantidadPropina.compareTo(BigDecimal.ZERO) < 0) {
                mostrarMensajeErrorDatoPropina("El campo cantidad de propina no puede ser negativo.");
            }
        } catch (TextoVacioException e) {
            mostrarMensajeErrorDatoPropina("El campo cantidad de propina no puede estar vacio.");
        } catch (NumeroInvalidoException e) {
            mostrarMensajeErrorDatoPropina("El campo cantidad de propina debe ser un número decimal válido.");
        }
        return;
    }

    private void actualizarPropina() {
        recolectarDatoPropina();
        if (this.errorEnRecolectorCantidadPropina) {
            return;
        }

        PedidoDAO pedidoDAO = new PedidoDAO();

        try {
            pedidoDAO.actualizarPropina(pedido.getCodigoPedido(), cantidadPropina);
            pedido.setPropina(cantidadPropina);
            actualizarTotal();
            jLabelPropinaActual.setText(" Propina actual : Q. " + cantidadPropina.toString());
        } catch (SQLException e) {
            String mensajeError = "Error al actualizar la propina: " + e.getMessage();
            mostrarMensajeError(mensajeError);
        }

    }

    private void mostrarMensajeErrorDatoPropina(String mensaje) {
        this.errorEnRecolectorCantidadPropina = true;
        MensajeDialogFrame mensajeDialog = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeDialog.setVisible(true);
    }

    private void mostrarMensajeError(String mensaje) {
        MensajeDialogFrame mensajeDialog = new MensajeDialogFrame(null, true, mensaje, true);
        mensajeDialog.setVisible(true);
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo1 = new javax.swing.JLabel();
        jSplitPane = new javax.swing.JSplitPane();
        jPanelAgregarDatos = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanelAgregarProductos = new javax.swing.JPanel();
        jLabelTituloAgregarProducto = new javax.swing.JLabel();
        jPanelFormularioAgregarProducto = new javax.swing.JPanel();
        jPanelElijaProducto = new javax.swing.JPanel();
        jLabelElijaProducto = new javax.swing.JLabel();
        jComboBoxProductos = new javax.swing.JComboBox<>();
        jPanelCantidadProducto = new javax.swing.JPanel();
        jLabelCantidad = new javax.swing.JLabel();
        jTextFieldCantidadProducto = new javax.swing.JTextField();
        jButtonAgregarProducto = new javax.swing.JButton();
        jPanelAgregarPropina = new javax.swing.JPanel();
        jLabelTituloAgregarPropina = new javax.swing.JLabel();
        jLabelTituloNotaPropina = new javax.swing.JLabel();
        jLabelPropinaActual = new javax.swing.JLabel();
        jPanelFormularioPropina = new javax.swing.JPanel();
        jPanelCantidadPropina = new javax.swing.JPanel();
        jLabelCantidadPropina = new javax.swing.JLabel();
        jTextFieldCantidadPropina = new javax.swing.JTextField();
        jButtonAgregarPropina = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanelConfirmarProductos = new javax.swing.JPanel();
        jLabelTituloConfirmar = new javax.swing.JLabel();
        jPanelListaSinConfirmar = new javax.swing.JPanel();
        jButtonConfirmar = new javax.swing.JButton();
        jPanelDetalles = new javax.swing.JPanel();
        jLabelDetallesCuenta = new javax.swing.JLabel();
        jPanelAjuste = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableDetalles = new javax.swing.JTable();
        jLabelTotal = new javax.swing.JLabel();
        jButtonCerrarCuenta = new javax.swing.JButton();

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTitulo1.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo1.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo1.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo1.setText("Cuenta Abierta No. xxxxx");
        jLabelTitulo1.setOpaque(true);
        jPanelTitulo.add(jLabelTitulo1);

        getContentPane().add(jPanelTitulo, java.awt.BorderLayout.NORTH);

        jPanelAgregarDatos.setMinimumSize(new java.awt.Dimension(550, 282));
        jPanelAgregarDatos.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanelAgregarProductos.setBackground(new java.awt.Color(50, 52, 35));
        jPanelAgregarProductos.setLayout(new java.awt.GridBagLayout());

        jLabelTituloAgregarProducto.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloAgregarProducto.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTituloAgregarProducto.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTituloAgregarProducto.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloAgregarProducto.setText("Agregar Producto");
        jLabelTituloAgregarProducto.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelAgregarProductos.add(jLabelTituloAgregarProducto, gridBagConstraints);

        jPanelFormularioAgregarProducto.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormularioAgregarProducto.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanelFormularioAgregarProducto.setLayout(new javax.swing.BoxLayout(jPanelFormularioAgregarProducto, javax.swing.BoxLayout.Y_AXIS));

        jPanelElijaProducto.setBackground(new java.awt.Color(50, 52, 35));
        jPanelElijaProducto.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelElijaProducto.setBackground(new java.awt.Color(50, 52, 35));
        jLabelElijaProducto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelElijaProducto.setText("Elija un producto");
        jLabelElijaProducto.setOpaque(true);
        jPanelElijaProducto.add(jLabelElijaProducto);

        jComboBoxProductos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxProductos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelElijaProducto.add(jComboBoxProductos);

        jPanelFormularioAgregarProducto.add(jPanelElijaProducto);

        jPanelCantidadProducto.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCantidadProducto.setLayout(new java.awt.GridLayout(0, 1, 2, 5));

        jLabelCantidad.setBackground(new java.awt.Color(50, 52, 35));
        jLabelCantidad.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCantidad.setText("Cantidad");
        jLabelCantidad.setOpaque(true);
        jPanelCantidadProducto.add(jLabelCantidad);

        jTextFieldCantidadProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelCantidadProducto.add(jTextFieldCantidadProducto);

        jPanelFormularioAgregarProducto.add(jPanelCantidadProducto);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanelAgregarProductos.add(jPanelFormularioAgregarProducto, gridBagConstraints);

        jButtonAgregarProducto.setBackground(new java.awt.Color(227, 135, 88));
        jButtonAgregarProducto.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonAgregarProducto.setText("Agregar");
        jButtonAgregarProducto.addActionListener(this::jButtonAgregarProductoActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanelAgregarProductos.add(jButtonAgregarProducto, gridBagConstraints);

        jPanel1.add(jPanelAgregarProductos);

        jPanelAgregarPropina.setBackground(new java.awt.Color(50, 52, 35));
        jPanelAgregarPropina.setLayout(new java.awt.GridBagLayout());

        jLabelTituloAgregarPropina.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloAgregarPropina.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTituloAgregarPropina.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTituloAgregarPropina.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloAgregarPropina.setText("Agregar Propina");
        jLabelTituloAgregarPropina.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelAgregarPropina.add(jLabelTituloAgregarPropina, gridBagConstraints);

        jLabelTituloNotaPropina.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloNotaPropina.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabelTituloNotaPropina.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTituloNotaPropina.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloNotaPropina.setText("La cantidad que ingrese, sera el nuevo valor de la propina");
        jLabelTituloNotaPropina.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanelAgregarPropina.add(jLabelTituloNotaPropina, gridBagConstraints);

        jLabelPropinaActual.setBackground(new java.awt.Color(50, 52, 35));
        jLabelPropinaActual.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabelPropinaActual.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPropinaActual.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelPropinaActual.setText(" Propina actual : Q. 00.00");
        jLabelPropinaActual.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 10;
        jPanelAgregarPropina.add(jLabelPropinaActual, gridBagConstraints);

        jPanelFormularioPropina.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormularioPropina.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanelFormularioPropina.setLayout(new javax.swing.BoxLayout(jPanelFormularioPropina, javax.swing.BoxLayout.Y_AXIS));

        jPanelCantidadPropina.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCantidadPropina.setLayout(new java.awt.GridLayout(0, 1, 2, 5));

        jLabelCantidadPropina.setBackground(new java.awt.Color(50, 52, 35));
        jLabelCantidadPropina.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCantidadPropina.setText("Cantidad");
        jLabelCantidadPropina.setOpaque(true);
        jPanelCantidadPropina.add(jLabelCantidadPropina);

        jTextFieldCantidadPropina.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelCantidadPropina.add(jTextFieldCantidadPropina);

        jPanelFormularioPropina.add(jPanelCantidadPropina);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 50;
        jPanelAgregarPropina.add(jPanelFormularioPropina, gridBagConstraints);

        jButtonAgregarPropina.setBackground(new java.awt.Color(227, 135, 88));
        jButtonAgregarPropina.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonAgregarPropina.setText("Agregar");
        jButtonAgregarPropina.addActionListener(this::jButtonAgregarPropinaActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        jPanelAgregarPropina.add(jButtonAgregarPropina, gridBagConstraints);

        jPanel1.add(jPanelAgregarPropina);

        jPanelAgregarDatos.add(jPanel1, java.awt.BorderLayout.CENTER);

        jSplitPane.setLeftComponent(jPanelAgregarDatos);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanelConfirmarProductos.setBackground(new java.awt.Color(50, 52, 35));
        jPanelConfirmarProductos.setLayout(new java.awt.GridBagLayout());

        jLabelTituloConfirmar.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloConfirmar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTituloConfirmar.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTituloConfirmar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloConfirmar.setText("Confirmar Productos");
        jLabelTituloConfirmar.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelConfirmarProductos.add(jLabelTituloConfirmar, gridBagConstraints);

        jPanelListaSinConfirmar.setBackground(new java.awt.Color(50, 52, 35));
        jPanelListaSinConfirmar.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanelListaSinConfirmar.setLayout(new java.awt.GridLayout(0, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanelConfirmarProductos.add(jPanelListaSinConfirmar, gridBagConstraints);

        jButtonConfirmar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonConfirmar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonConfirmar.setText("Confirmar");
        jButtonConfirmar.addActionListener(this::jButtonConfirmarActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanelConfirmarProductos.add(jButtonConfirmar, gridBagConstraints);

        jPanel3.add(jPanelConfirmarProductos);

        jPanelDetalles.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDetalles.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelDetalles.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelDetalles.setLayout(new java.awt.GridBagLayout());

        jLabelDetallesCuenta.setBackground(new java.awt.Color(50, 52, 35));
        jLabelDetallesCuenta.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelDetallesCuenta.setForeground(new java.awt.Color(227, 135, 88));
        jLabelDetallesCuenta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelDetallesCuenta.setText("Detalles de la cuenta");
        jLabelDetallesCuenta.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanelDetalles.add(jLabelDetallesCuenta, gridBagConstraints);

        jPanelAjuste.setBackground(new java.awt.Color(50, 52, 35));
        jPanelAjuste.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jPanelAjuste.setLayout(new java.awt.BorderLayout());

        jTableDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTableDetalles);

        jPanelAjuste.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelDetalles.add(jPanelAjuste, gridBagConstraints);

        jLabelTotal.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTotal.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTotal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTotal.setText(" Total (Incluye propina) : Q. 00.00");
        jLabelTotal.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipady = 10;
        jPanelDetalles.add(jLabelTotal, gridBagConstraints);

        jButtonCerrarCuenta.setBackground(new java.awt.Color(227, 135, 88));
        jButtonCerrarCuenta.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonCerrarCuenta.setText("Cerrar Cuenta");
        jButtonCerrarCuenta.addActionListener(this::jButtonCerrarCuentaActionPerformed);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanelDetalles.add(jButtonCerrarCuenta, gridBagConstraints);

        jPanel3.add(jPanelDetalles);
        jPanelDetalles.getAccessibleContext().setAccessibleParent(jSplitPane);

        jPanel2.add(jPanel3, java.awt.BorderLayout.CENTER);

        jSplitPane.setRightComponent(jPanel2);

        getContentPane().add(jSplitPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonConfirmarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonConfirmarActionPerformed
        if (productosPedidoSinConfirmar.isEmpty()) {
            mostrarMensajeError("No hay productos pendientes de confirmar.");
            return;
        }

        try {
            BigDecimal montoTotal = this.cuentasServicio.confirmarProductos(pedido, productosPedidoSinConfirmar);
            pedido.setMontoPedido(montoTotal);
            productosPedidoSinConfirmar.clear();
            actualizarPanelesProductosSinConfirmar();
            cargarDatosTabla();
            actualizarTotal();
        } catch (StockInsuficienteException e) {
            String mensajeError = e.getMessage();
            mostrarMensajeError(mensajeError);
        } catch (SQLException e) {
            String mensajeError = "Error inesperado al confirmar los productos: " + e.getMessage();
            mostrarMensajeError(mensajeError);
        }
    }// GEN-LAST:event_jButtonConfirmarActionPerformed

    private void jButtonCerrarCuentaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCerrarCuentaActionPerformed
        if (!productosPedidoSinConfirmar.isEmpty()) {
            mostrarMensajeError("No se puede cerrar la cuenta con productos pendientes de confirmar.");
            return;
        }

        try {
            this.cuentasServicio.cerrarCuenta(pedido);
            String mensajeExito = "Cuenta cerrada exitosamente.";
            MensajeDialogFrame mensajeDialog = new MensajeDialogFrame(null, true, mensajeExito, false);
            mensajeDialog.setVisible(true);
            this.dispose();
        } catch (SQLException e) {
            String mensajeError = "Error al cerrar la cuenta: " + e.getMessage();
            mostrarMensajeError(mensajeError);
        }
    }// GEN-LAST:event_jButtonCerrarCuentaActionPerformed

    private void jButtonAgregarPropinaActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAgregarPropinaActionPerformed
        actualizarPropina();
    }// GEN-LAST:event_jButtonAgregarPropinaActionPerformed

    private void jButtonAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonAgregarProductoActionPerformed
        actualizarListaProductosSinConfirmar();
        actualizarPanelesProductosSinConfirmar();
    }// GEN-LAST:event_jButtonAgregarProductoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAgregarProducto;
    private javax.swing.JButton jButtonAgregarPropina;
    private javax.swing.JButton jButtonCerrarCuenta;
    private javax.swing.JButton jButtonConfirmar;
    private javax.swing.JComboBox<String> jComboBoxProductos;
    private javax.swing.JLabel jLabelCantidad;
    private javax.swing.JLabel jLabelCantidadPropina;
    private javax.swing.JLabel jLabelDetallesCuenta;
    private javax.swing.JLabel jLabelElijaProducto;
    private javax.swing.JLabel jLabelPropinaActual;
    private javax.swing.JLabel jLabelTitulo1;
    private javax.swing.JLabel jLabelTituloAgregarProducto;
    private javax.swing.JLabel jLabelTituloAgregarPropina;
    private javax.swing.JLabel jLabelTituloConfirmar;
    private javax.swing.JLabel jLabelTituloNotaPropina;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelAgregarDatos;
    private javax.swing.JPanel jPanelAgregarProductos;
    private javax.swing.JPanel jPanelAgregarPropina;
    private javax.swing.JPanel jPanelAjuste;
    private javax.swing.JPanel jPanelCantidadProducto;
    private javax.swing.JPanel jPanelCantidadPropina;
    private javax.swing.JPanel jPanelConfirmarProductos;
    private javax.swing.JPanel jPanelDetalles;
    private javax.swing.JPanel jPanelElijaProducto;
    private javax.swing.JPanel jPanelFormularioAgregarProducto;
    private javax.swing.JPanel jPanelFormularioPropina;
    private javax.swing.JPanel jPanelListaSinConfirmar;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JTable jTableDetalles;
    private javax.swing.JTextField jTextFieldCantidadProducto;
    private javax.swing.JTextField jTextFieldCantidadPropina;
    // End of variables declaration//GEN-END:variables
}

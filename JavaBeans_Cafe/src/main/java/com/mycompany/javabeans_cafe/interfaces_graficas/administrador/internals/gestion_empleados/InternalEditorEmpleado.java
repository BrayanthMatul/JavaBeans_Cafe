/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestion_empleados;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.mycompany.javabeans_cafe.daos.EmpleadoDAO;
import com.mycompany.javabeans_cafe.enums.EmpleadoRol;
import com.mycompany.javabeans_cafe.enums.JornadaLaboral;
import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import com.mycompany.javabeans_cafe.interfaces_graficas.modales.MensajeDialogFrame;
import com.mycompany.javabeans_cafe.modelos.Empleado;
import com.mycompany.javabeans_cafe.util.RecolectorDeDatos;
import com.mycompany.javabeans_cafe.util.VerificadorDatosEmpleado;

/**
 *
 * @author matul
 */
public class InternalEditorEmpleado extends javax.swing.JInternalFrame {

    private RecolectorDeDatos recolector;
    private boolean errorEnRecolector;
    private boolean errorEnValidacion;
    private String dpi;
    private String nombreCompleto;
    private String nombreUsuario;
    private String contrasena;
    private BigDecimal salario;
    private EmpleadoRol rol;
    private JornadaLaboral jornadaLaboral;
    private List<Empleado> empleadosCargados = new ArrayList<>();
    private Empleado empleadoSeleccionado;

    /**
     * Creates new form InternalSeleccionadorEditorEmpleado
     */
    public InternalEditorEmpleado() {
        initComponents();

        jSplitPane1.setResizeWeight(0.3);

        SwingUtilities.invokeLater(() -> {
            jSplitPane1.setDividerLocation(0.3);
        });

        cargarRoles();
        cargarJornadas();
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        jComboBoxEmpleados.removeAllItems();
        empleadosCargados.clear();

        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        try {
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            empleadosCargados.addAll(empleados);
            for (Empleado empleado : empleados) {
                jComboBoxEmpleados.addItem(empleado.getDpi() + " - " + empleado.getNombreUsuario());
            }
        } catch (SQLException e) {
            String mensajeError = "Error al cargar empleados: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }

    }

    private void cargarRoles() {
        jComboBoxTipoEmpleado.removeAllItems();

        for (EmpleadoRol rol : EmpleadoRol.values()) {
            jComboBoxTipoEmpleado.addItem(rol.name());
        }
    }

    private void cargarJornadas() {
        jComboBoxJornadaLaboral.removeAllItems();

        for (JornadaLaboral jornada : JornadaLaboral.values()) {
            jComboBoxJornadaLaboral.addItem(jornada.name());
        }
    }

    private void recolectarDatosUsuario() {
        this.errorEnRecolector = false;
        recolector = new RecolectorDeDatos();
        String mensajeError = "";

        try {
            this.dpi = recolector.recolectarTexto(jTextFieldDPI);
        } catch (TextoVacioException e) {
            mensajeError = "El campo DPI no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.nombreCompleto = recolector.recolectarTexto(jTextFieldNombreCompleto);
        } catch (TextoVacioException e) {
            mensajeError = "El campo nombre completo no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.nombreUsuario = recolector.recolectarTexto(jTextFieldNombreUsuario);
        } catch (TextoVacioException e) {
            mensajeError = "El campo nombre de usuario no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.contrasena = recolector.recolectarTexto(jTextFieldContrasenia);
        } catch (TextoVacioException e) {
            mensajeError = "El campo contraseña no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        try {
            this.salario = recolector.recolectarBigDecimals(jTextFieldSalario);
        } catch (TextoVacioException e) {
            mensajeError = "El campo salario no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo salario contiene un número inválido.";
            mostrarMensajeErrorRecolector(mensajeError);
            return;
        }

        this.rol = convertirStringARol((String) jComboBoxTipoEmpleado.getSelectedItem());
        this.jornadaLaboral = convertirStringAJornada((String) jComboBoxJornadaLaboral.getSelectedItem());

    }

    private EmpleadoRol convertirStringARol(String rolString) {
        return EmpleadoRol.valueOf(rolString);
    }

    private JornadaLaboral convertirStringAJornada(String jornadaString) {
        return JornadaLaboral.valueOf(jornadaString);
    }

    private void validarDatosEmpleado() {
        errorEnValidacion = false;
        String mensajeError = "";

        VerificadorDatosEmpleado verificador = new VerificadorDatosEmpleado();

        try {

            if (!empleadoSeleccionado.getDpi().equals(dpi) && verificador.existeDPI(dpi)) {
                mensajeError = "Ya existe un usuario con ese DPI, por favor elija otro";
                mostrarMensajeErrorValidador(mensajeError);
                return;
            }

            if (!empleadoSeleccionado.getNombreUsuario().equals(nombreUsuario)
                    && verificador.existeNombreUsuario(nombreUsuario)) {
                mensajeError = "Ya existe un usuario con ese nombre, por favor elija otro";
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanelSeleccionador = new javax.swing.JPanel();
        jPanelTitulo1 = new javax.swing.JPanel();
        jLabelTituloSeleccionar = new javax.swing.JLabel();
        jPanelCentrado1 = new javax.swing.JPanel();
        jPanelFormulario1 = new javax.swing.JPanel();
        jPanelDPI13 = new javax.swing.JPanel();
        jLabelLista = new javax.swing.JLabel();
        jComboBoxEmpleados = new javax.swing.JComboBox<>();
        jPanelBoton1 = new javax.swing.JPanel();
        jButtonSeleccionar = new javax.swing.JButton();
        jPanelEditor = new javax.swing.JPanel();
        jPanelTitulo = new javax.swing.JPanel();
        jLabelTituloEditar = new javax.swing.JLabel();
        jPanelCentrado = new javax.swing.JPanel();
        jPanelFormulario = new javax.swing.JPanel();
        jPanelDPI7 = new javax.swing.JPanel();
        jLabelDPI = new javax.swing.JLabel();
        jTextFieldDPI = new javax.swing.JTextField();
        jPanelDPI = new javax.swing.JPanel();
        jLabelNombreCompleto = new javax.swing.JLabel();
        jTextFieldNombreCompleto = new javax.swing.JTextField();
        jPanelDPI1 = new javax.swing.JPanel();
        jLabelNombreUsuario = new javax.swing.JLabel();
        jTextFieldNombreUsuario = new javax.swing.JTextField();
        jPanelDPI3 = new javax.swing.JPanel();
        jLabelContrasenia = new javax.swing.JLabel();
        jTextFieldContrasenia = new javax.swing.JTextField();
        jPanelDPI4 = new javax.swing.JPanel();
        jLabelSalario = new javax.swing.JLabel();
        jTextFieldSalario = new javax.swing.JTextField();
        jPanelDPI5 = new javax.swing.JPanel();
        jLabelSalarioTipoEmpleado = new javax.swing.JLabel();
        jComboBoxTipoEmpleado = new javax.swing.JComboBox<>();
        jPanelDPI6 = new javax.swing.JPanel();
        jLabelJornadaLaboral = new javax.swing.JLabel();
        jComboBoxJornadaLaboral = new javax.swing.JComboBox<>();
        jPanelBoton = new javax.swing.JPanel();
        jButtonEditar = new javax.swing.JButton();

        jPanelSeleccionador.setLayout(new java.awt.BorderLayout());

        jPanelTitulo1.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTituloSeleccionar.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloSeleccionar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTituloSeleccionar.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTituloSeleccionar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloSeleccionar.setText("Seleccionar Empleado");
        jLabelTituloSeleccionar.setOpaque(true);
        jPanelTitulo1.add(jLabelTituloSeleccionar);

        jPanelSeleccionador.add(jPanelTitulo1, java.awt.BorderLayout.NORTH);

        jPanelCentrado1.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentrado1.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentrado1.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentrado1.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario1.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario1.setLayout(new javax.swing.BoxLayout(jPanelFormulario1, javax.swing.BoxLayout.Y_AXIS));

        jPanelDPI13.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI13.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelLista.setBackground(new java.awt.Color(50, 52, 35));
        jLabelLista.setForeground(new java.awt.Color(255, 255, 255));
        jLabelLista.setText("Elija un empleado ");
        jLabelLista.setOpaque(true);
        jPanelDPI13.add(jLabelLista);

        jComboBoxEmpleados.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxEmpleados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI13.add(jComboBoxEmpleados);

        jPanelFormulario1.add(jPanelDPI13);

        jPanelCentrado1.add(jPanelFormulario1, new java.awt.GridBagConstraints());

        jPanelSeleccionador.add(jPanelCentrado1, java.awt.BorderLayout.CENTER);

        jPanelBoton1.setBackground(new java.awt.Color(50, 52, 35));

        jButtonSeleccionar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonSeleccionar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonSeleccionar.setText("Seleccionar");
        jButtonSeleccionar.addActionListener(this::jButtonSeleccionarActionPerformed);
        jPanelBoton1.add(jButtonSeleccionar);

        jPanelSeleccionador.add(jPanelBoton1, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setLeftComponent(jPanelSeleccionador);

        jPanelEditor.setLayout(new java.awt.BorderLayout());

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTituloEditar.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTituloEditar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTituloEditar.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTituloEditar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTituloEditar.setText("Editor de Empleado");
        jLabelTituloEditar.setOpaque(true);
        jPanelTitulo.add(jLabelTituloEditar);

        jPanelEditor.add(jPanelTitulo, java.awt.BorderLayout.NORTH);

        jPanelCentrado.setBackground(new java.awt.Color(50, 52, 35));
        jPanelCentrado.setMinimumSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setPreferredSize(new java.awt.Dimension(550, 343));
        jPanelCentrado.setLayout(new java.awt.GridBagLayout());

        jPanelFormulario.setBackground(new java.awt.Color(50, 52, 35));
        jPanelFormulario.setLayout(new javax.swing.BoxLayout(jPanelFormulario, javax.swing.BoxLayout.Y_AXIS));

        jPanelDPI7.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI7.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelDPI.setBackground(new java.awt.Color(50, 52, 35));
        jLabelDPI.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDPI.setText("DPI");
        jLabelDPI.setOpaque(true);
        jPanelDPI7.add(jLabelDPI);

        jTextFieldDPI.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jTextFieldDPI.setDisabledTextColor(new java.awt.Color(50, 52, 35));
        jPanelDPI7.add(jTextFieldDPI);

        jPanelFormulario.add(jPanelDPI7);

        jPanelDPI.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelNombreCompleto.setBackground(new java.awt.Color(50, 52, 35));
        jLabelNombreCompleto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNombreCompleto.setText("Nombre Completo");
        jLabelNombreCompleto.setOpaque(true);
        jPanelDPI.add(jLabelNombreCompleto);

        jTextFieldNombreCompleto
                .setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI.add(jTextFieldNombreCompleto);

        jPanelFormulario.add(jPanelDPI);

        jPanelDPI1.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI1.setLayout(new java.awt.GridLayout(0, 1, 2, 5));

        jLabelNombreUsuario.setBackground(new java.awt.Color(50, 52, 35));
        jLabelNombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNombreUsuario.setText("Nombre de Usuario");
        jLabelNombreUsuario.setOpaque(true);
        jPanelDPI1.add(jLabelNombreUsuario);

        jTextFieldNombreUsuario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI1.add(jTextFieldNombreUsuario);

        jPanelFormulario.add(jPanelDPI1);

        jPanelDPI3.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI3.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelContrasenia.setBackground(new java.awt.Color(50, 52, 35));
        jLabelContrasenia.setForeground(new java.awt.Color(255, 255, 255));
        jLabelContrasenia.setText("Contrasena");
        jLabelContrasenia.setOpaque(true);
        jPanelDPI3.add(jLabelContrasenia);

        jTextFieldContrasenia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI3.add(jTextFieldContrasenia);

        jPanelFormulario.add(jPanelDPI3);

        jPanelDPI4.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI4.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelSalario.setBackground(new java.awt.Color(50, 52, 35));
        jLabelSalario.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSalario.setText("Salario");
        jLabelSalario.setOpaque(true);
        jPanelDPI4.add(jLabelSalario);

        jTextFieldSalario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI4.add(jTextFieldSalario);

        jPanelFormulario.add(jPanelDPI4);

        jPanelDPI5.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI5.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelSalarioTipoEmpleado.setBackground(new java.awt.Color(50, 52, 35));
        jLabelSalarioTipoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSalarioTipoEmpleado.setText("Tipo de Empleado");
        jLabelSalarioTipoEmpleado.setOpaque(true);
        jPanelDPI5.add(jLabelSalarioTipoEmpleado);

        jComboBoxTipoEmpleado.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxTipoEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI5.add(jComboBoxTipoEmpleado);

        jPanelFormulario.add(jPanelDPI5);

        jPanelDPI6.setBackground(new java.awt.Color(50, 52, 35));
        jPanelDPI6.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jLabelJornadaLaboral.setBackground(new java.awt.Color(50, 52, 35));
        jLabelJornadaLaboral.setForeground(new java.awt.Color(255, 255, 255));
        jLabelJornadaLaboral.setText("Jornada Laboral");
        jLabelJornadaLaboral.setOpaque(true);
        jPanelDPI6.add(jLabelJornadaLaboral);

        jComboBoxJornadaLaboral.setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxJornadaLaboral.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(227, 135, 88)));
        jPanelDPI6.add(jComboBoxJornadaLaboral);

        jPanelFormulario.add(jPanelDPI6);

        jPanelCentrado.add(jPanelFormulario, new java.awt.GridBagConstraints());

        jPanelEditor.add(jPanelCentrado, java.awt.BorderLayout.CENTER);

        jPanelBoton.setBackground(new java.awt.Color(50, 52, 35));

        jButtonEditar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonEditar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonEditar.setText("Editar");
        jButtonEditar.addActionListener(this::jButtonEditarActionPerformed);
        jPanelBoton.add(jButtonEditar);

        jPanelEditor.add(jPanelBoton, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setRightComponent(jPanelEditor);

        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonEditarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonEditarActionPerformed
        if (empleadoSeleccionado == null) {
            String mensajeError = "Por favor seleccione un empleado antes de editar.";
            mostrarMensajeErrorValidador(mensajeError);
            return;
        }

        recolectarDatosUsuario();
        if (errorEnRecolector) {
            return;
        }

        validarDatosEmpleado();

        if (errorEnValidacion) {
            return;
        }

        EmpleadoDAO empleadoDAO = new EmpleadoDAO();

        Empleado empleadoActualizado = new Empleado(
                empleadoSeleccionado.getCodigoEmpleado(),
                dpi,
                nombreCompleto,
                nombreUsuario,
                contrasena,
                rol,
                jornadaLaboral,
                salario,
                empleadoSeleccionado.getFechaContratacion(),
                empleadoSeleccionado.isActivo());

        try {
            empleadoDAO.actualizarEmpleado(empleadoActualizado);
            empleadoSeleccionado = null;
            cargarEmpleados();
            limpiarCampos();
            String mensajeExito = "Empleado actualizado exitosamente.";
            MensajeDialogFrame frameExito = new MensajeDialogFrame(null, true, mensajeExito, false);
            frameExito.setVisible(true);
        } catch (SQLException e) {
            String mensajeError = "Error al actualizar el empleado: " + e.getMessage();
            MensajeDialogFrame frameError = new MensajeDialogFrame(null, true, mensajeError, true);
            frameError.setVisible(true);
        }
    }// GEN-LAST:event_jButtonEditarActionPerformed

    private void limpiarCampos() {
        jTextFieldDPI.setText("");
        jTextFieldNombreCompleto.setText("");
        jTextFieldNombreUsuario.setText("");
        jTextFieldContrasenia.setText("");
        jTextFieldSalario.setText("");
        jComboBoxTipoEmpleado.setSelectedIndex(0);
        jComboBoxJornadaLaboral.setSelectedIndex(0);
    }

    private void jButtonSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonSeleccionarActionPerformed
        int indiceSeleccionado = jComboBoxEmpleados.getSelectedIndex();

        if (indiceSeleccionado == -1) {
            return;
        }

        this.empleadoSeleccionado = empleadosCargados.get(indiceSeleccionado);

        cargarDatosEmpleado();
    }// GEN-LAST:event_jButtonSeleccionarActionPerformed

    private void cargarDatosEmpleado() {
        jTextFieldDPI.setText(empleadoSeleccionado.getDpi());
        jTextFieldNombreCompleto.setText(empleadoSeleccionado.getNombreCompleto());
        jTextFieldNombreUsuario.setText(empleadoSeleccionado.getNombreUsuario());
        jTextFieldContrasenia.setText(empleadoSeleccionado.getContrasena());
        jTextFieldSalario.setText(empleadoSeleccionado.getSalario().toString());
        jComboBoxTipoEmpleado.setSelectedItem(empleadoSeleccionado.getRol().name());
        jComboBoxJornadaLaboral.setSelectedItem(empleadoSeleccionado.getJornadaLaboral().name());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonEditar;
    private javax.swing.JButton jButtonSeleccionar;
    private javax.swing.JComboBox<String> jComboBoxEmpleados;
    private javax.swing.JComboBox<String> jComboBoxJornadaLaboral;
    private javax.swing.JComboBox<String> jComboBoxTipoEmpleado;
    private javax.swing.JLabel jLabelContrasenia;
    private javax.swing.JLabel jLabelDPI;
    private javax.swing.JLabel jLabelJornadaLaboral;
    private javax.swing.JLabel jLabelLista;
    private javax.swing.JLabel jLabelNombreCompleto;
    private javax.swing.JLabel jLabelNombreUsuario;
    private javax.swing.JLabel jLabelSalario;
    private javax.swing.JLabel jLabelSalarioTipoEmpleado;
    private javax.swing.JLabel jLabelTituloEditar;
    private javax.swing.JLabel jLabelTituloSeleccionar;
    private javax.swing.JPanel jPanelBoton;
    private javax.swing.JPanel jPanelBoton1;
    private javax.swing.JPanel jPanelCentrado;
    private javax.swing.JPanel jPanelCentrado1;
    private javax.swing.JPanel jPanelDPI;
    private javax.swing.JPanel jPanelDPI1;
    private javax.swing.JPanel jPanelDPI13;
    private javax.swing.JPanel jPanelDPI3;
    private javax.swing.JPanel jPanelDPI4;
    private javax.swing.JPanel jPanelDPI5;
    private javax.swing.JPanel jPanelDPI6;
    private javax.swing.JPanel jPanelDPI7;
    private javax.swing.JPanel jPanelEditor;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelFormulario1;
    private javax.swing.JPanel jPanelSeleccionador;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JPanel jPanelTitulo1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextFieldContrasenia;
    private javax.swing.JTextField jTextFieldDPI;
    private javax.swing.JTextField jTextFieldNombreCompleto;
    private javax.swing.JTextField jTextFieldNombreUsuario;
    private javax.swing.JTextField jTextFieldSalario;
    // End of variables declaration//GEN-END:variables
}

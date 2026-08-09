/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestion_empleados;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

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
public class InternalRegistroEmpleado extends javax.swing.JInternalFrame {

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

    /**
     * Creates new form InternalRegistrarEmpleado
     */
    public InternalRegistroEmpleado() {
        initComponents();

        this.errorEnRecolector = false;
        this.errorEnValidacion = false;

        jLabelTitulo.setText("Registrar Nuevo Empleado");
        jButtonRealizar.setText("Registrar");
        cargarRoles();
        cargarJornadas();
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

            if (verificador.existeDPI(dpi)) {
                mensajeError = "Ya existe un usuario con ese DPI, por favor elija otro";
                mostrarMensajeErrorValidador(mensajeError);
                return;
            }

            if (verificador.existeNombreUsuario(nombreUsuario)) {
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

    private void guardarEmpleado() {
        Empleado nuevoEmpleado = new Empleado(dpi, nombreCompleto, nombreUsuario, contrasena, rol, jornadaLaboral,
                salario, LocalDate.now(), true);
        EmpleadoDAO registradorEmpleado = new EmpleadoDAO();

        try {
            registradorEmpleado.insertarEmpleado(nuevoEmpleado);
            String mensaje = "Empleado creado exitosamente.";
            MensajeDialogFrame mensajeExitoFrame = new MensajeDialogFrame(null, true, mensaje, false);
            mensajeExitoFrame.setVisible(true);
            limpiarCampos();
        } catch (SQLException e) {
            e.printStackTrace();
            String mensaje = "Error al crear el empleado: " + e.getMessage();
            MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
            mensajeErrorFrame.setVisible(true);
        }
    }

    private void limpiarCampos() {
        jTextFieldDPI.setText("");
        jTextFieldNombreCompleto.setText("");
        jTextFieldNombreUsuario.setText("");
        jTextFieldContrasenia.setText("");
        jTextFieldSalario.setText("");
        jComboBoxTipoEmpleado.setSelectedIndex(0);
        jComboBoxJornadaLaboral.setSelectedIndex(0);
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

        jPanelTitulo = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
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
        jButtonRealizar = new javax.swing.JButton();

        jPanelTitulo.setBackground(new java.awt.Color(50, 52, 35));

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Registrar Nuevo Empleado");
        jLabelTitulo.setOpaque(true);
        jPanelTitulo.add(jLabelTitulo);

        getContentPane().add(jPanelTitulo, java.awt.BorderLayout.NORTH);

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

        getContentPane().add(jPanelCentrado, java.awt.BorderLayout.CENTER);

        jPanelBoton.setBackground(new java.awt.Color(50, 52, 35));

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Registrar Empleado");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanelBoton.add(jButtonRealizar);

        getContentPane().add(jPanelBoton, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonRealizarActionPerformed
        recolectarDatosUsuario();

        if (errorEnRecolector) {
            return;
        }

        validarDatosEmpleado();

        if (errorEnValidacion) {
            return;
        }

        guardarEmpleado();
    }// GEN-LAST:event_jButtonRealizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JComboBox<String> jComboBoxJornadaLaboral;
    private javax.swing.JComboBox<String> jComboBoxTipoEmpleado;
    private javax.swing.JLabel jLabelContrasenia;
    private javax.swing.JLabel jLabelDPI;
    private javax.swing.JLabel jLabelJornadaLaboral;
    private javax.swing.JLabel jLabelNombreCompleto;
    private javax.swing.JLabel jLabelNombreUsuario;
    private javax.swing.JLabel jLabelSalario;
    private javax.swing.JLabel jLabelSalarioTipoEmpleado;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelBoton;
    private javax.swing.JPanel jPanelCentrado;
    private javax.swing.JPanel jPanelDPI;
    private javax.swing.JPanel jPanelDPI1;
    private javax.swing.JPanel jPanelDPI3;
    private javax.swing.JPanel jPanelDPI4;
    private javax.swing.JPanel jPanelDPI5;
    private javax.swing.JPanel jPanelDPI6;
    private javax.swing.JPanel jPanelDPI7;
    private javax.swing.JPanel jPanelFormulario;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JTextField jTextFieldContrasenia;
    private javax.swing.JTextField jTextFieldDPI;
    private javax.swing.JTextField jTextFieldNombreCompleto;
    private javax.swing.JTextField jTextFieldNombreUsuario;
    private javax.swing.JTextField jTextFieldSalario;
    // End of variables declaration//GEN-END:variables
}

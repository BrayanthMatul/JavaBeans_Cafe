/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.mycompany.javabeans_cafe.interfaces_graficas.administrador.internals.gestion_empleados;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

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
    private boolean esEdicion;
    /**
     * Creates new form InternalRegistrarEmpleado
     */
    public InternalEditorEmpleado() {
        initComponents();

        this.errorEnRecolector = false;
        this.errorEnValidacion = false;
        this.esEdicion = false;

        jLabelTitulo.setText("Registrar Nuevo Empleado");
        jButtonRealizar.setText("Registrar");
        cargarRoles();
        cargarJornadas();
    }

    private void cargarRoles() {
        // limpiar el combo box
        jComboBoxTipoEmpleado.removeAllItems();
        // cargar los roles disponibles
        jComboBoxTipoEmpleado.addItem("ADMINISTRADOR");
        jComboBoxTipoEmpleado.addItem("BARISTA");
        jComboBoxTipoEmpleado.addItem("COCINA");
        jComboBoxTipoEmpleado.addItem("MESERO");
    }

    private void cargarJornadas() {
        // limpiar el combo box
        jComboBoxJornadaLaboral.removeAllItems();
        // cargar las jornadas disponibles
        jComboBoxJornadaLaboral.addItem("MATUTINA");
        jComboBoxJornadaLaboral.addItem("VESPERTINA");
        jComboBoxJornadaLaboral.addItem("NOCTURNA");
    }

    private void recolectarDatosUsuario() {
        this.errorEnRecolector = false;
        recolector = new RecolectorDeDatos();
        String mensajeError = "";

        try {
            this.dpi = recolector.recolectarTexto(jTextFieldNombreUsuario);
        } catch (TextoVacioException e) {
            mensajeError = "El campo DPI no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
        }

        try {
            this.nombreCompleto = recolector.recolectarTexto(jTextFieldNombreCompleto);
        } catch (TextoVacioException e) {
            mensajeError = "El campo nombre completo no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
        }

        try {
            this.nombreUsuario = recolector.recolectarTexto(jTextFieldNombreUsuario);
        } catch (TextoVacioException e) {
            mensajeError = "El campo nombre de usuario no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
        }

        try {
            this.contrasena = recolector.recolectarTexto(jTextFieldContrasenia);
        } catch (TextoVacioException e) {
            mensajeError = "El campo contraseña no puede estar vacio.";
            mostrarMensajeErrorRecolector(mensajeError);
        }

        try {
            this.salario = recolector.recolectarBigDecimals(jTextFieldSalario);
        } catch (NumeroInvalidoException e) {
            mensajeError = "El campo salario contiene un número inválido.";
            mostrarMensajeErrorRecolector(mensajeError);
        }

        this.rol = convertirStringARol((String) jComboBoxTipoEmpleado.getSelectedItem());
        this.jornadaLaboral = convertirStringASucursal((String) jComboBoxJornadaLaboral.getSelectedItem());

    }

    private EmpleadoRol convertirStringARol(String rolString) {
        if (rolString.equals("ADMINISTRADOR")) {
            return EmpleadoRol.ADMINISTRADOR;
        } else if (rolString.equals("BARISTA")) {
            return EmpleadoRol.BARISTA;
        } else if (rolString.equals("COCINA")) {
            return EmpleadoRol.COCINA;
        } else if (rolString.equals("MESERO")) {
            return EmpleadoRol.MESERO;
        } else {
            return null;
        }
    }

    private JornadaLaboral convertirStringASucursal(String jornadaString) {
        if (jornadaString.equals("MATUTINA")) {
            return JornadaLaboral.MATUTINA;
        } else if (jornadaString.equals("VESPERTINA")) {
            return JornadaLaboral.VESPERTINA;
        } else if (jornadaString.equals("NOCTURNA")) {
            return JornadaLaboral.NOCTURNA;
        } else {
            return null;
        }
    }

     private void validarDatosEmpleado() {
        errorEnValidacion = false;
        VerificadorDatosEmpleado verificadorDatosEmpleado = new VerificadorDatosEmpleado();
        try {
            verificadorDatosEmpleado.verificarDatos(nombreUsuario);
            if (verificadorDatosEmpleado.getExisteNombreUsuario() && !esEdicion) {
                errorEnValidacion = true;
                String mensajeError = "Ya existe un usuario con ese nombre, por favor elija otro";
                mostrarMensajeErrorValidador(mensajeError);
            }
        } catch (SQLException e) {
            String mensajeErrorBD = "Error al verificar los datos en la base de datos";
            mostrarMensajeErrorValidador(mensajeErrorBD);
        }
    }

    private void mostrarMensajeErrorRecolector(String mensaje) {
        MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje,true);
        mensajeErrorFrame.setVisible(true);
        errorEnRecolector = true;
    }

    private void mostrarMensajeErrorValidador(String mensaje) {
        MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje,true);
        mensajeErrorFrame.setVisible(true);
        errorEnRecolector = true;
    }

    private void guardarEmpleado() {
        Empleado nuevoUsuario = new Empleado(dpi, nombreCompleto, nombreUsuario, contrasena, rol, jornadaLaboral, salario, null, true);
        EmpleadoDAO registradorEmpleado = new EmpleadoDAO();

        try {
            registradorEmpleado.insertarEmpleado(nuevoUsuario);
            String mensaje = "Empleado creado exitosamente.";
            MensajeDialogFrame mensajeExitoFrame = new MensajeDialogFrame(null, true, mensaje, false);
            mensajeExitoFrame.setVisible(true);
            limpiarCampos();
        } catch (SQLException e) {
            String mensaje = "Error al crear el usuario: " + e.getMessage();
            MensajeDialogFrame mensajeErrorFrame = new MensajeDialogFrame(null, true, mensaje, true);
            mensajeErrorFrame.setVisible(true);
        }
    }

    private void actualizarUsuario() {
        // Implementar la lógica para actualizar un usuario existente
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jPanelDPI2 = new javax.swing.JPanel();
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
        jButtonRealizar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();

        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBackground(new java.awt.Color(50, 52, 35));
        jPanel1.setLayout(new java.awt.GridLayout(1, 3));

        jPanel3.setBackground(new java.awt.Color(50, 52, 35));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 844, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3);

        jPanel2.setBackground(new java.awt.Color(50, 52, 35));
        jPanel2.setLayout(new java.awt.GridLayout(0, 1, 0, 20));

        jLabelTitulo.setBackground(new java.awt.Color(50, 52, 35));
        jLabelTitulo.setFont(new java.awt.Font("Noto Sans CJK JP Black", 1, 15)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(227, 135, 88));
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Registrar Nuevo Empleado");
        jLabelTitulo.setOpaque(true);
        jPanel2.add(jLabelTitulo);

        jPanelDPI2.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelDPI.setBackground(new java.awt.Color(50, 52, 35));
        jLabelDPI.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDPI.setText("DPI");
        jLabelDPI.setOpaque(true);
        jPanelDPI2.add(jLabelDPI);
        jPanelDPI2.add(jTextFieldDPI);

        jPanel2.add(jPanelDPI2);

        jPanelDPI.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelNombreCompleto.setBackground(new java.awt.Color(50, 52, 35));
        jLabelNombreCompleto.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNombreCompleto.setText("Nombre Completo");
        jLabelNombreCompleto.setOpaque(true);
        jPanelDPI.add(jLabelNombreCompleto);
        jPanelDPI.add(jTextFieldNombreCompleto);

        jPanel2.add(jPanelDPI);

        jPanelDPI1.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelNombreUsuario.setBackground(new java.awt.Color(50, 52, 35));
        jLabelNombreUsuario.setForeground(new java.awt.Color(255, 255, 255));
        jLabelNombreUsuario.setText("Nombre de Usuario");
        jLabelNombreUsuario.setOpaque(true);
        jPanelDPI1.add(jLabelNombreUsuario);
        jPanelDPI1.add(jTextFieldNombreUsuario);

        jPanel2.add(jPanelDPI1);

        jPanelDPI3.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelContrasenia.setBackground(new java.awt.Color(50, 52, 35));
        jLabelContrasenia.setForeground(new java.awt.Color(255, 255, 255));
        jLabelContrasenia.setText("Contrasena");
        jLabelContrasenia.setOpaque(true);
        jPanelDPI3.add(jLabelContrasenia);
        jPanelDPI3.add(jTextFieldContrasenia);

        jPanel2.add(jPanelDPI3);

        jPanelDPI4.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelSalario.setBackground(new java.awt.Color(50, 52, 35));
        jLabelSalario.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSalario.setText("Salario");
        jLabelSalario.setOpaque(true);
        jPanelDPI4.add(jLabelSalario);
        jPanelDPI4.add(jTextFieldSalario);

        jPanel2.add(jPanelDPI4);

        jPanelDPI5.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelSalarioTipoEmpleado.setBackground(new java.awt.Color(50, 52, 35));
        jLabelSalarioTipoEmpleado.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSalarioTipoEmpleado.setText("Tipo de Empleado");
        jLabelSalarioTipoEmpleado.setOpaque(true);
        jPanelDPI5.add(jLabelSalarioTipoEmpleado);

        jComboBoxTipoEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanelDPI5.add(jComboBoxTipoEmpleado);

        jPanel2.add(jPanelDPI5);

        jPanelDPI6.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        jLabelJornadaLaboral.setBackground(new java.awt.Color(50, 52, 35));
        jLabelJornadaLaboral.setForeground(new java.awt.Color(255, 255, 255));
        jLabelJornadaLaboral.setText("Jornada Laboral");
        jLabelJornadaLaboral.setOpaque(true);
        jPanelDPI6.add(jLabelJornadaLaboral);

        jComboBoxJornadaLaboral.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanelDPI6.add(jComboBoxJornadaLaboral);

        jPanel2.add(jPanelDPI6);

        jButtonRealizar.setBackground(new java.awt.Color(227, 135, 88));
        jButtonRealizar.setFont(new java.awt.Font("Noto Sans CJK JP Black", 0, 12)); // NOI18N
        jButtonRealizar.setText("Registrar Empleado");
        jButtonRealizar.addActionListener(this::jButtonRealizarActionPerformed);
        jPanel2.add(jButtonRealizar);
        jPanel2.add(jLabel1);

        jPanel1.add(jPanel2);

        jPanel4.setBackground(new java.awt.Color(50, 52, 35));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 335, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 844, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel4);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonRealizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRealizarActionPerformed
       recolectarDatosUsuario();

        if (!errorEnRecolector) {
            validarDatosEmpleado();
            if (!errorEnValidacion) {
                if (!esEdicion) {
                    guardarEmpleado();
                } else {
                    actualizarUsuario();
                }
            }
        }
    }//GEN-LAST:event_jButtonRealizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonRealizar;
    private javax.swing.JComboBox<String> jComboBoxJornadaLaboral;
    private javax.swing.JComboBox<String> jComboBoxTipoEmpleado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelContrasenia;
    private javax.swing.JLabel jLabelDPI;
    private javax.swing.JLabel jLabelJornadaLaboral;
    private javax.swing.JLabel jLabelNombreCompleto;
    private javax.swing.JLabel jLabelNombreUsuario;
    private javax.swing.JLabel jLabelSalario;
    private javax.swing.JLabel jLabelSalarioTipoEmpleado;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelDPI;
    private javax.swing.JPanel jPanelDPI1;
    private javax.swing.JPanel jPanelDPI2;
    private javax.swing.JPanel jPanelDPI3;
    private javax.swing.JPanel jPanelDPI4;
    private javax.swing.JPanel jPanelDPI5;
    private javax.swing.JPanel jPanelDPI6;
    private javax.swing.JTextField jTextFieldContrasenia;
    private javax.swing.JTextField jTextFieldDPI;
    private javax.swing.JTextField jTextFieldNombreCompleto;
    private javax.swing.JTextField jTextFieldNombreUsuario;
    private javax.swing.JTextField jTextFieldSalario;
    // End of variables declaration//GEN-END:variables
}

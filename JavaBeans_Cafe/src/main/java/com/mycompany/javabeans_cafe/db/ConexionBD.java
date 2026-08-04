package com.mycompany.javabeans_cafe.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/javabeans_cafe";
    private static final String USER = "usuario_practica_1";
    private static final String PASSWORD = "123456";
    private static Connection conexion;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Error al conectar a la base de datos", e);
        }
    }

    private ConexionBD() {
    }

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            synchronized (ConexionBD.class) {
                if (conexion == null || conexion.isClosed()) {
                    conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
        }
        return conexion;
    }

}

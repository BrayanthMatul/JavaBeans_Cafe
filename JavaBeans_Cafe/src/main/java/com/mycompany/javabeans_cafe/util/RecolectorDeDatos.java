/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javabeans_cafe.util;

import com.mycompany.javabeans_cafe.exceptions.NumeroInvalidoException;
import com.mycompany.javabeans_cafe.exceptions.TextoVacioException;
import javax.swing.text.JTextComponent;

/**
 *
 * @author matul
 */
public class RecolectorDeDatos {

    public String recolectarTexto(JTextComponent campo)
            throws TextoVacioException {

        return obtenerTextoNoVacio(campo);
    }

    public int recolectarEntero(JTextComponent campo)
            throws TextoVacioException, NumeroInvalidoException {

        String texto = obtenerTextoNoVacio(campo);

        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new NumeroInvalidoException();
        }
    }

    private String obtenerTextoNoVacio(JTextComponent campo)
            throws TextoVacioException {

        String texto = campo.getText();

        if (texto.isBlank()) {
            throw new TextoVacioException();
        }

        return texto.strip();
    }
}

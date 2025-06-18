/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package UI.Controladores;

import Dominio.Usuario;

/**
 *
 * @author ianco
 */
public interface LoginGestorView {
    
    // Métodos para obtener datos de la vista
    String getUsuario();
    String getContrasena();
    
    // Métodos para mostrar información en la vista
    void mostrarError(String mensaje);
    void limpiarError();
    void limpiarCampos();
    
    // Métodos para controlar la vista
    void mostrarVentana();
    void cerrarVentana();
    void habilitarFormulario(boolean habilitar);
    
    // Método para navegar a la siguiente ventana
    void abrirSiguienteVentana(Usuario usuario);
}
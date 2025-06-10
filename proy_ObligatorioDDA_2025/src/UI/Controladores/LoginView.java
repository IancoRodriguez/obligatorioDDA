/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package UI.Controladores;

import Dominio.Dispositivo;
import Dominio.Excepciones.DispositivoException;

/**
 *
 * @author ianco
 */

public interface LoginView {
    /**
     * Obtiene el nombre de usuario ingresado
     * @return String con el nombre de usuario
     */
    String getUsuario();
    
    /**
     * Obtiene la contraseña ingresada
     * @return String con la contraseña
     */
    String getContrasena();
    
    /**
     * Obtiene el dispositivo asociado a esta vista
     * @return Dispositivo de la vista
     */
    Dispositivo getDispositivo();
    
    /**
     * Muestra un mensaje de error en la vista
     * @param mensaje Mensaje a mostrar
     */
    void mostrarError(String mensaje);
    
    /**
     * Actualiza el estado visual de login
     * @param estado true si está logueado, false si no
     */
    void setLogueado(boolean estado);
    
    /**
     * Limpia los campos de entrada de la vista
     */
    void limpiarCampos();
    
    /**
     * Limpia la sesión en el dispositivo
     * @throws DispositivoException si hay error al limpiar
     */
    void limpiarSesionDispositivo() throws DispositivoException;
}
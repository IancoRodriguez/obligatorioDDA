/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.Controladores;

import Dominio.Dispositivo;
import Dominio.Excepciones.DispositivoException;
import Dominio.Excepciones.UsuarioException;
import Dominio.Servicio;
import Servicios.Fachada;

/**
 *
 * @author ianco
 */


public class LoginControlador {
    private final LoginView view;
    private final Fachada fachada;
    private final Dispositivo dispositivo;
    private Servicio servicioActual;
    
    // Constructor que acepta vista y dispositivo
    public LoginControlador(LoginView view, Dispositivo dispositivo) {
        this.view = view;
        this.fachada = Fachada.getInstancia();
        this.dispositivo = dispositivo;
    }
    
    /**
     * Procesa el intento de login del usuario
     * Valida credenciales y maneja el estado de la sesión
     */
    public void procesarLogin() {
        try {
            String usuario = view.getUsuario();
            String contrasena = view.getContrasena();
            
            // Validación básica en el controlador
            if (usuario == null || usuario.isBlank() || 
                contrasena == null || contrasena.isBlank()) {
                throw new UsuarioException("Revise las credenciales ingresadas");
            }
            
            // Procesar login a través de la fachada
            servicioActual = fachada.loginCliente(usuario, contrasena, dispositivo);
            
            // Actualizar vista en caso de éxito
            view.setLogueado(true);
            view.mostrarError("");
            
        } catch (UsuarioException | DispositivoException ex) {
            // Manejar errores y actualizar vista
            view.mostrarError(ex.getMessage());
            view.setLogueado(false);
            view.limpiarCampos();
            servicioActual = null;
        }
    }
    
    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        try {
            view.setLogueado(false);
            view.limpiarCampos();
            view.mostrarError("");
            
            servicioActual = null;
            dispositivo.setServicioActivo(null);
            
        } catch (DispositivoException ex) {
            view.mostrarError(ex.getMessage());
        }
    }
    
    /**
     * Verifica si hay una sesión activa
     * @return true si hay un servicio activo
     */
    public boolean tieneServicioActivo() {
        return servicioActual != null;
    }
    

    
    /**
     * Obtiene el servicio actual
     * @return Servicio actual o null si no hay sesión activa
     */
    public Servicio getServicioActual() {
        return view.getDispositivo().getServicioActivo();
    }
}
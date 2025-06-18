
package UI.Controladores;

import Dominio.Excepciones.UsuarioException;
import Dominio.Usuario;
import Servicios.Fachada;


public class LoginGestorControlador {
    
    private LoginGestorView vista;
    private Fachada fachada;
    
    public LoginGestorControlador(LoginGestorView vista) {
        this.vista = vista;
        this.fachada = Fachada.getInstancia();
    }
    
    /**
     * Maneja el intento de login
     */
    public void manejarLogin() {
        try {
            // Validar campos vacíos
            if (!validarCampos()) {
                return;
            }
            
            // Deshabilitar formulario durante el proceso
            vista.habilitarFormulario(false);
            vista.limpiarError();
            
            // Obtener datos de la vista
            String usuario = vista.getUsuario();
            String contrasena = vista.getContrasena();
            
            // Intentar login a través del modelo
            Usuario usuarioLogueado = fachada.loginGestor(usuario, contrasena);
            
            if (usuarioLogueado != null) {
                // Login exitoso
                vista.limpiarCampos();
                vista.abrirSiguienteVentana(usuarioLogueado);
                vista.cerrarVentana();
            } else {
                // Login fallido
                vista.mostrarError("Usuario o contraseña inválidos");
                vista.habilitarFormulario(true);
            }
            
        } catch (UsuarioException ex) {
            // Error de negocio
            vista.mostrarError(ex.getMessage());
            vista.habilitarFormulario(true);
        } catch (Exception ex) {
            // Error inesperado
            vista.mostrarError("Error inesperado: " + ex.getMessage());
            vista.habilitarFormulario(true);
        }
    }
    
    /**
     * Valida que los campos no estén vacíos
     */
    private boolean validarCampos() {
        String usuario = vista.getUsuario();
        String contrasena = vista.getContrasena();
        
        if (usuario == null || usuario.trim().isEmpty()) {
            vista.mostrarError("El campo usuario es obligatorio");
            return false;
        }
        
        if (contrasena == null || contrasena.trim().isEmpty()) {
            vista.mostrarError("El campo contraseña es obligatorio");
            return false;
        }
        
        return true;
    }
    
    /**
     * Maneja la cancelación del login
     */
    public void manejarCancelar() {
        vista.limpiarCampos();
        vista.limpiarError();
        vista.cerrarVentana();
    }
}

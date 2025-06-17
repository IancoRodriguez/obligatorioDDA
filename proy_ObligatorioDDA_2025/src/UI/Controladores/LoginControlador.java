package UI.Controladores;

import Dominio.Dispositivo;
import Dominio.Excepciones.DispositivoException;
import Dominio.Excepciones.UsuarioException;
import Dominio.Servicio;
import Servicios.Fachada;




public class LoginControlador {
    private final ClienteView view;
    private final Fachada fachada;
    private final Dispositivo dispositivo;
    private Servicio servicioActual;
    
    // Constructor que acepta vista y dispositivo
    public LoginControlador(ClienteView view, Dispositivo dispositivo) {
        this.view = view;
        this.fachada = Fachada.getInstancia();
        this.dispositivo = dispositivo;
    }
    
    
    public void procesarLogin() {
        try {
            String usuario = view.getUsuario();
            String contrasena = view.getContrasena();
            
            if (usuario == null || usuario.isBlank() || 
                contrasena == null || contrasena.isBlank()) {
                throw new UsuarioException("Revise las credenciales ingresadas");
            }
            
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
   
    public boolean tieneServicioActivo() {
        return servicioActual != null;
    }
    
    public Servicio getServicioActual() {
        return view.getDispositivo().getServicioActivo();
    }
}
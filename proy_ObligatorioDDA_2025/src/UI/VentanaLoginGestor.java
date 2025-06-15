
package UI;

import Dominio.Excepciones.UsuarioException;
import Dominio.Gestor;
import Dominio.Usuario;
import UI.Controladores.LoginGestorControlador;



public class VentanaLoginGestor extends VentanaLogin{
    
    private LoginGestorControlador controlador;
    
    public VentanaLoginGestor(){
        this.controlador = new LoginGestorControlador();
    }

    @Override
    public void abrirSiguienteVentana(Usuario gestor) {
        new GestorUI( (Gestor) gestor).setVisible(true);
    }

    @Override
    public Usuario login(String usuario, String contrasena) throws UsuarioException {
        try {
            return controlador.loginGestor(usuario, contrasena);
        } catch (UsuarioException ex) {
            throw ex;
        } 

    }
}

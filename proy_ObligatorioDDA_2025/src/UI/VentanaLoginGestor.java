
package UI;

import Dominio.Excepciones.UsuarioException;
import Dominio.Gestor;
import Dominio.Usuario;
import UI.Controladores.LoginGestorControlador;



public class VentanaLoginGestor extends VentanaLogin {
    
    public VentanaLoginGestor() {
        super();
        // Inicializar el controlador pasando esta vista
        this.controlador = new LoginGestorControlador(this);
    }

    @Override
    public void abrirSiguienteVentana(Usuario gestor) {
        new GestorUI((Gestor) gestor).setVisible(true);
    }

    @Override
    public Usuario login(String usuario, String contrasena) throws UsuarioException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

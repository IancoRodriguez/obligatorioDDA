
package UI.Controladores;

import Dominio.Excepciones.UsuarioException;
import Dominio.Gestor;
import Dominio.Usuario;
import Servicios.Fachada;


public class LoginGestorControlador 
{
    private Fachada f;
    
    public LoginGestorControlador(){
        
        this.f = Fachada.getInstancia();
    }

    public Usuario loginGestor(String usuario, String contrasena) throws UsuarioException{
        return Fachada.getInstancia().loginGestor(usuario, contrasena);
    }
    
    
    
    
    
}

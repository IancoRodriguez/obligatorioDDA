/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Dominio.Excepciones.DispositivoException;
import Dominio.Excepciones.UsuarioException;
import Dominio.Gestor;
import Dominio.Usuario;
import Servicios.Fachada;
import java.util.logging.Level;
import java.util.logging.Logger;




public class VentanaLoginGestor extends VentanaLogin{
    

    @Override
    public void abrirSiguienteVentana(Usuario gestor) {
        new GestorUI( (Gestor) gestor).setVisible(true);
    }

    @Override
    public Usuario login(String usuario, String contrasena) {
        try {
            return Fachada.getInstancia().loginGestor(usuario, contrasena);
        } catch (UsuarioException ex) {
            Logger.getLogger(VentanaLoginGestor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DispositivoException ex) {
            Logger.getLogger(VentanaLoginGestor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
        
    }
}

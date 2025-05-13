/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Dominio.Gestor;
import Dominio.Usuario;
import Servicios.Fachada;




public class VentanaLoginGestor extends VentanaLogin{
    

    @Override
    public void abrirSiguienteVentana(Usuario gestor) {
        new GestorUI( (Gestor) gestor).setVisible(true);
    }

    @Override
    public Usuario login(String usuario, String contrasena) {
        return Fachada.getInstancia().loginGestor(usuario, contrasena);
        
        
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Usuario;
import Servicios.Fachada;
  



public class VentanaLoginCliente extends VentanaLogin{
    private Dispositivo dispositivo;

    public VentanaLoginCliente(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }
    

    @Override
    public void abrirSiguienteVentana(Usuario cliente) {
        new ClienteUI( (Cliente) cliente).setVisible(true);
    }

    @Override
    public Usuario login(String usuario, String contrasena) {
        return Fachada.getInstancia().loginCliente(usuario, contrasena);
        
        
    }
}

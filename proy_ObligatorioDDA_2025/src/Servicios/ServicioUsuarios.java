/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Cliente;
import Dominio.Gestor;
import Dominio.Usuario;
import java.util.ArrayList;
import java.util.List;


public class ServicioUsuarios {
    private List<Cliente> clientes;
    private List<Gestor> gestores;
    
    
    public ServicioUsuarios() {
        clientes = new ArrayList();
        gestores = new ArrayList();
       
    }
    
    public boolean agregar(Cliente cli) {
        clientes.add(cli);
        return true;
    }
    
    public boolean agregar(Gestor gestor) {
        gestores.add(gestor);
        return true;
    }
    
    
    
    public Cliente loginCliente(String nombre, String contrasena) {
        return (Cliente) login(nombre, contrasena, this.clientes);
    }

    public Gestor loginGestor(String nombre, String contrasena){
        return (Gestor) login(nombre, contrasena, this.gestores);
    }
    
    
    private Usuario login(String nombre, String contrasena, List<? extends Usuario> listaUsuarios) {
        if(nombre == null || nombre.isEmpty() || contrasena == null || contrasena.isEmpty())
            return null;
        
        for(Usuario usuario : listaUsuarios)
            if(usuario.getLoginId().equals(nombre) && usuario.isContrasenaValida(contrasena))
                return usuario;
        
        return null;
    }

    
    // Geters y Setters 
    
    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Gestor> getGestores() {
        return gestores;
    }
    
    
}

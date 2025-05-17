/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Cliente;
import Dominio.Gestor;
import Dominio.Ingreso;
import Dominio.Usuario;
import java.util.ArrayList;
import java.util.List;


public class ServicioUsuarios {
    private List<Cliente> clientes;
    private List<Gestor> gestores;
    private List<Ingreso> ingresos;
    
    
    
    public ServicioUsuarios() {
        clientes = new ArrayList();
        gestores = new ArrayList();
        ingresos = new ArrayList();
       
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
        
        Usuario u = null;
        boolean isLogueado = false;
        
        // Busco el usuario en el listado de usuarios
        for(Usuario usuario : listaUsuarios)
            if(usuario.getLoginId().equals(nombre) && usuario.isContrasenaValida(contrasena)){
                u = usuario;
            }
        
        
        // Verifico que el usuario no este logueado
        for(Ingreso i : ingresos){
            if(i.getCliente().equals(u)){
                isLogueado = true;
                break;
            }
        }
        
        // Si no esta logueado, le genero el ingreso y lo devuelvo
        if(!isLogueado && u != null){
            Ingreso i = new Ingreso(u);
            ingresos.add(i);
            return u;
        }
        
        // Si el usuario no cumple las validaciones o esta logueado, devolvemos null
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Excepciones.DispositivoException;
import Dominio.Excepciones.UsuarioException;
import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Gestor;
import Dominio.Ingreso;
import Dominio.Pedido;
import Dominio.Servicio;
import Dominio.Usuario;
import java.util.ArrayList;
import java.util.List;

public class ServicioUsuarios {

    private List<Cliente> clientes;
    private List<Gestor> gestores;
//    private Fachada f = Fachada.getInstancia();

    public ServicioUsuarios() {
        clientes = new ArrayList();
        gestores = new ArrayList();
 //       f = Fachada.getInstancia();

    }

    public boolean agregar(Cliente cli) {
        clientes.add(cli);
        return true;
    }

    public boolean agregar(Gestor gestor) {
        gestores.add(gestor);
        return true;
    }

    public Servicio loginCliente(String nombre, String contrasena, Dispositivo dispositivo) throws UsuarioException, DispositivoException {
        
        Cliente cliente = (Cliente) login(nombre, contrasena, this.clientes);
        if(cliente != null){
            Servicio s = new Servicio(cliente);
            cliente.setServicio(s);
            dispositivo.setServicioActivo(s);
            return s;
        }
        
        throw new UsuarioException("Usuario no registrado.");
    }

    public Gestor loginGestor(String nombre, String contrasena) throws UsuarioException, DispositivoException {
        return (Gestor) login(nombre, contrasena, this.gestores);
    }

    private Usuario login(String nombre, String contrasena, List<? extends Usuario> listaUsuarios) throws UsuarioException, DispositivoException {
        if (nombre == null || nombre.isEmpty() || contrasena == null || contrasena.isEmpty()) {
            return null;
        }

        // Verifico que el usuario no este logueado
        for (Dispositivo d : Fachada.getInstancia().getDispositivos()) {
            if (d.getServicioActivo() != null && d.clienteLogueado().getLoginId().equals(nombre)) 
                throw new UsuarioException("El usuario ya esta logueado");
        }

       

        // Busco el usuario en el listado de usuarios
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getLoginId().equals(nombre) && usuario.isContrasenaValida(contrasena)) {
                return usuario;
            }
        }

        // Si el usuario no cumple las validaciones o esta logueado, devolvemos null
        return null;
    }

    //metodos gestores
    public void tomarPedido(Gestor gestor, Pedido p) {
        gestor.setPedidosTomados(p);
    }

    // Geters y Setters 
    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Gestor> getGestores() {
        return gestores;
    }

}


package Servicios;

import Dominio.Categoria;
import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Gestor;
import Dominio.Item;
import Dominio.Menu;
import Dominio.Pedido;
import Dominio.Servicio;
import java.util.List;

public class Fachada {

    private static Fachada instancia;
    private ServicioUsuarios sUsuarios;
    private ServicioPedidos sPedidos;
    private ServicioDispositivos sDispositivos;
    private ServicioGestor sGestor;
    
    
    private Fachada() {
        this.sUsuarios = new ServicioUsuarios();
        this.sPedidos = new ServicioPedidos();
        this.sDispositivos = new ServicioDispositivos();
     }

    public static Fachada getInstancia() {
        if (instancia == null) {
            synchronized (Fachada.class) {
                if (instancia == null) {
                    instancia = new Fachada();
                }
            }
        }
        return instancia;
    }

    public boolean agregar(Cliente cli) {
        return sUsuarios.agregar(cli);
    }

    public boolean agregar(Gestor gestor) {
        return sUsuarios.agregar(gestor);
    }

    public Cliente loginCliente(String nombre, String contrasena) {
        return sUsuarios.loginCliente(nombre, contrasena);
    }

    public Gestor loginGestor(String nombre, String contrasena) {
        return sUsuarios.loginGestor(nombre, contrasena);
    }

    public boolean agregar(Dispositivo d) {
        return sDispositivos.agregar(d);
    }
    
    //Getters y setters 

    public List<Dispositivo> getDispositivos() {
        return sDispositivos.getDispositivos();
    }

    public List<Cliente> getClientes() {
        return sUsuarios.getClientes();
    }

    public List<Gestor> getGestores() {
        return sUsuarios.getGestores();
    }

    public Pedido registrarPedido(Item item, String comentario, Servicio servicio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Pedido> getPedidosPendientes(String nombreUP) {
        return sPedidos.getPedidosPendientesUP(nombreUP);
    }

    // La idea de este metodo es tomar el pedido seleccionado, 
    // sumarlo al listado que tiene el gestor de pedidos y cambiarle el estado en el listado de sPedidos
    public void tomarPedido(Gestor gestor, Pedido p) {
        sGestor.tomarPedido(gestor, p);
        sPedidos.cambiarEstado(p);
    }
 
    /*
    
    Unificar el manejo de los estados de los pedidos para no repetir codigo solo para cambiar el tipo de estado
    public void finalizarPedido(Gestor gestor, Pedido p) {
        sGestor.finalizarPedido(gestor, p);
        sPedidos.finalizarPedido(p);
    }
    
    public void entregaarPedido(Gestor gestor, Pedido p) {
        sGestor.finalizarPedido(gestor, p);
        sPedidos.finalizarPedido(p);
    }
    */
     
    
    
    
}


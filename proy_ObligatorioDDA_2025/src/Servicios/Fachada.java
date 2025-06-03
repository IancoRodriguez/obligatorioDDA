package Servicios;

import Dominio.Categoria;
import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Excepciones.DispositivoException;
import Dominio.Excepciones.UsuarioException;
import Dominio.Gestor;
import Dominio.Item;
import Dominio.Menu;
import Dominio.Pedido;
import Dominio.Servicio;
import Dominio.UnidadProcesadora;
import java.util.List;

public class Fachada {

    private static Fachada instancia;
    private ServicioUsuarios sUsuarios;
    private ServicioDispositivos sDispositivos;

    
    
    private Fachada() {
        this.sUsuarios = new ServicioUsuarios();
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
 
    
    public Servicio loginCliente(String nombre, String contrasena, Dispositivo d) throws UsuarioException, DispositivoException {
        return sUsuarios.loginCliente(nombre, contrasena, d);
    }

    public Gestor loginGestor(String nombre, String contrasena) throws UsuarioException, DispositivoException {
        return sUsuarios.loginGestor(nombre, contrasena);
    }

    public boolean agregar(Dispositivo d) {
        return sDispositivos.agregar(d);
    }
    
    //Getters y setters 

    public List<Pedido> getTodosLosPedidos() {
        return sDispositivos.getTodosLosPedidos();
    }
    

    public List<Dispositivo> getDispositivos() {
        return sDispositivos.getDispositivos();
    }

    public List<Cliente> getClientes() {
        return sUsuarios.getClientes();
    }

    public List<Gestor> getGestores() {
        return sUsuarios.getGestores();
    }

   

    // La idea de este metodo es tomar el pedido seleccionado, 
    // sumarlo al listado que tiene el gestor de pedidos y cambiarle el estado en el listado de sPedidos
    public void tomarPedido(Gestor gestor, Pedido p) {
        sUsuarios.tomarPedido(gestor, p);
        //sPedidos.cambiarEstado(p);
    }

    public List<Pedido> getPedidosConfirmados(String nombreUP) {
        return sDispositivos.getPedidosConfirmados(nombreUP);
    }
    

    public List<Servicio> getServicios() {
        return sDispositivos.getServicios();
        
    }
     
    
    
    
}


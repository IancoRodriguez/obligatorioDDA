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
    private ControlServicios cServicios;
    private ServicioDispositivos sDispositivos;
    private ServicioMenus sMenus;
    private ServicioPedidos sPedidos;
    
    
    private Fachada() {
        this.sUsuarios = new ServicioUsuarios();
        this.cServicios = new ControlServicios(sPedidos);
        this.sDispositivos = new ServicioDispositivos();
        this.sMenus = new ServicioMenus();
        this.sPedidos = new ServicioPedidos();
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
    
    
    // Servicio Menus 

    public void setMenuActivo(Menu menu) {
        sMenus.setMenuActivo(menu);
    }

    public Menu getMenuActivo() {
        return sMenus.getMenuActivo();
    }

    public List<Menu> getMenus() {
        return sMenus.getMenus();
    }

    public void agregarMenu(Menu menu) {
        sMenus.agregarMenu(menu);
    }

    public List<Categoria> obtenerCategorias() {
        return sMenus.obtenerCategorias();
    }

    public List<Item> obtenerItemsDeCategoria(String nombreCategoria) {
        return sMenus.obtenerItemsDeCategoria(nombreCategoria);
    }

    public void agregarItem(String nombreCategoria, Item item) {
        sMenus.agregarItem(nombreCategoria, item);
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

   
    
    
    
}


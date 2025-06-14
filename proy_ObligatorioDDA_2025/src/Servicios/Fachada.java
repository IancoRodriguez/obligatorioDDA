package Servicios;

import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Excepciones.DispositivoException;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.UsuarioException;
import Dominio.Gestor;
import Dominio.Observer.Observable;
import Dominio.Observer.Observador;
import Dominio.Pedido;
import Dominio.Servicio;
import java.util.List;

public class Fachada extends Observable implements Observador {

    private static Fachada instancia;
    private ServicioUsuarios sUsuarios;
    private ServicioDispositivos sDispositivos;

    private Fachada() {
        this.sUsuarios = new ServicioUsuarios();
        this.sDispositivos = new ServicioDispositivos();

        this.sUsuarios.subscribir(this);
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

    public Servicio loginCliente(String nombre, String contrasena, Dispositivo d)
            throws UsuarioException, DispositivoException {
        return sUsuarios.loginCliente(nombre, contrasena, d);
    }

    public Gestor loginGestor(String nombre, String contrasena) throws UsuarioException {
        return sUsuarios.loginGestor(nombre, contrasena);
    }

    public boolean agregar(Dispositivo d) {
        return sDispositivos.agregar(d);
    }

    // Getters y setters

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

    
    public void tomarPedido(Gestor gestor, Pedido p) throws ServicioException {
        sUsuarios.tomarPedido(gestor, p);        
    }

    public List<Pedido> getPedidosConfirmados(String nombreUP) {
        return sDispositivos.getPedidosConfirmados(nombreUP);
    }

    public List<Servicio> getServicios() {
        return sDispositivos.getServicios();

    }

    @Override
    public void notificar(Observable origen, Object evento) {
        if (evento instanceof Observable.Evento && evento == Observable.Evento.NUEVO_SERVICIO) {
            notificar(Evento.NUEVO_SERVICIO);
        }
    }

}

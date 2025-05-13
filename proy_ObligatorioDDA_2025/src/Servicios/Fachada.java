package Servicios;

import Dominio.Cliente;
import Dominio.Gestor;

public class Fachada {

    private static Fachada instancia;
    private ServicioUsuarios sUsuarios;
    private ServicioServicios sServicios;

    private Fachada() {
        this.sUsuarios = new ServicioUsuarios();
        this.sServicios = new ServicioServicios();
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
    
    
}

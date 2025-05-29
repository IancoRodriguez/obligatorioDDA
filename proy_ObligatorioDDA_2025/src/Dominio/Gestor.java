package Dominio;

import java.util.List;

public class Gestor extends Usuario {

    private String nombreUsuario;

    private String contrasena;

    private String nombreCompleto;
    
    private List<Pedido> pedidosTomados;

    
    
    private UnidadProcesadora UP;
    

    public Gestor(String nombreUsuario, String contrasena, String nombreCompleto, UnidadProcesadora UP) {
        super(nombreCompleto, contrasena);
        this.nombreUsuario = nombreUsuario;
        this.UP = UP;
    }
    
    @Override
    public String getLoginId() {
        return this.nombreUsuario;
    }

    public String getNombreUP() {
        return UP.getNombre();
    }
    
    public UnidadProcesadora getUP() {
        return UP;
    }
    
    public List<Pedido> getPedidosTomados() {
        return pedidosTomados;
    }

    public void setPedidosTomados(Pedido pedido) {
        this.pedidosTomados.add(pedido);
    }
}

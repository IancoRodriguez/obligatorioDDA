package Dominio;

public class Gestor extends Usuario {

    private String nombreUsuario;

    private String contrasena;

    private String nombreCompleto;

    private Pedido[] pedido;

    public Gestor(String nombreUsuario, String contrasena, String nombreCompleto) {
        super(nombreCompleto, contrasena);
        this.nombreUsuario = nombreUsuario;
       
    }
    
    @Override
    public String getLoginId() {
        return this.nombreUsuario;
    }

}

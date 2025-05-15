package Dominio;

public class Cliente extends Usuario {

    private String numero;
    private TipoCliente tipoCliente;

    public Cliente(String nombreCompleto, String contrasena, String numero, TipoCliente t) {
        super(nombreCompleto, contrasena);
        this.numero = numero;
        this.tipoCliente = t;
    }
    
     @Override
    public String getLoginId() {
        return this.numero;
    }
    

}

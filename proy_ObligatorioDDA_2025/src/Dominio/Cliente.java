package Dominio;

public class Cliente extends Usuario {

    private String numero;

    public Cliente(String nombreCompleto, String contrasena, String numero) {
        super(nombreCompleto, contrasena);
        this.numero = numero;
    }
    
     @Override
    public String getLoginId() {
        return this.numero;
    }
    

}

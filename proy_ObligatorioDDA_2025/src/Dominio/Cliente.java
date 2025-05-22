package Dominio;

import Dominio.Tipos.TipoCliente;

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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    
    

}

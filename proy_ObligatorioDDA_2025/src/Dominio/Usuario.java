
package Dominio;

public abstract class Usuario {
    private String nombreCompleto;
    private String contrasena;

    
    
    public Usuario(String nombreCompleto, String contrasena) {
        
        this.nombreCompleto = nombreCompleto;
        this.contrasena = contrasena;
        
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
    
    public boolean isContrasenaValida(String contrasena) {
        return this.contrasena.equals(contrasena);
    }
    
    public abstract String getLoginId();
}

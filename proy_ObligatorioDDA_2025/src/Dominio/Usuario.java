/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author usuar
 */
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

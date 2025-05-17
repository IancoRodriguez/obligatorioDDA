/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author bruno
 */
public class Ingreso {

    
    private LocalDateTime fecha;
    private Usuario usuario;
    
    // Constructor
    public Ingreso(Usuario usuario){ 
        
        this.fecha = LocalDateTime.now();
        this.usuario = usuario;
    }
    
    // Getter
    public Usuario getCliente() {
        return usuario;
    }

    // Setter
    public void setCliente(Usuario usuario) {
        this.usuario = usuario;
    }
}

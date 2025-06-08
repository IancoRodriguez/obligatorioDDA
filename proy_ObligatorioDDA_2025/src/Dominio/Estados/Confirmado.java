/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ianco
 */
public class Confirmado implements EstadoPedido {
    private Pedido pedido;

    public Confirmado(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar(Pedido pedido) {
        System.out.println("ya esta conf");
              
    }

    public void procesar() {
        System.out.println("Pedido en procesamiento.");
        pedido.setEstado(new Procesando(pedido));
    }

    public void entregar() {
        System.out.println("No se puede entregar sin procesar.");
    }

    public void finalizar() {
        System.out.println("No se puede finalizar sin entregar.");
    }

    public void validarEliminacion() {
  
    }
     
    @Override
    public String toString() {
        return "Confirmado";
    }

 
    @Override
    public List<Ingrediente> ingredientesParaConfirmar(Pedido pedido) {
        return Collections.emptyList();
    }
    
    

    
}

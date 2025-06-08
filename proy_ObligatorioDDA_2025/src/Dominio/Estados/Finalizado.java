/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ianco
 */
public class Finalizado implements EstadoPedido{
    private Pedido pedido;

    public Finalizado(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar(Pedido pedido) {
        System.out.println("Ya finalizado. No se puede confirmar.");
    }

    public void procesar() {
        System.out.println("Ya fue procesado.");
    }

    public void entregar() {
        System.out.println("Ya fue entregado.");
    }

    public void finalizar() {
        System.out.println("Ya está finalizado.");
    }

    public void validarEliminacion() throws ServicioException {
        throw new ServicioException("No se puede eliminar un pedido finalizado.");
    }

    @Override
    public String toString() {
        return "Finalizado";
    }

 

    @Override
    public List<Ingrediente> ingredientesParaConfirmar(Pedido pedido) {
       return Collections.emptyList();
    }
    
    
}

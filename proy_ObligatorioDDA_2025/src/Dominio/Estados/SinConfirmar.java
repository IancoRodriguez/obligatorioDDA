/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Estados;

import Dominio.Pedido;
import java.util.List;

/**
 *
 * @author ianco
 */
public class SinConfirmar implements EstadoPedido {
    private Pedido pedido;

    public SinConfirmar(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() {
        System.out.println("Pedido confirmado.");
        pedido.setEstado(new Confirmado(pedido));
    }

    public void desconfirmar() {
        System.out.println("Ya está sin confirmar.");
    }

    public void procesar() {
        System.out.println("No se puede procesar un pedido sin confirmar.");
    }

    public void entregar() {
        System.out.println("No se puede entregar un pedido sin confirmar.");
    }

    public void finalizar() {
        System.out.println("No se puede finalizar un pedido sin confirmar.");
    }

    public void validarEliminacion() {
        // Se puede eliminar
    }

    @Override
    public void agregarSiEsConfirmado(Pedido pedido, List<Pedido> pedidos, String nombreUP) {
        
    }

    @Override
    public String toString() {
        return "Sin Confirmar";
    }
    
    
    

}

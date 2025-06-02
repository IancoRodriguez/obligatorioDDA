/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Pedido;
import java.util.List;

/**
 *
 * @author ianco
 */
public class Procesando implements EstadoPedido{
    private Pedido pedido;

    public Procesando(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() {
        System.out.println("Ya fue confirmado.");
    }

    public void desconfirmar() {
        System.out.println("No se puede desconfirmar un pedido en procesamiento.");
    }

    public void procesar() {
        System.out.println("Ya está en procesamiento.");
    }

    public void entregar() {
        System.out.println("Pedido entregado.");
        pedido.setEstado((EstadoPedido) new Entregado(pedido));
    }

    public void finalizar() {
        System.out.println("No se puede finalizar sin entregar.");
    }

    public void validarEliminacion() throws ServicioException {
        throw new ServicioException("No se puede eliminar un pedido en procesamiento.");
    }

    @Override
    public void agregarSiEsConfirmado(Pedido pedido, List<Pedido> pedidos, String nombreUP) {
        
    }

   

}

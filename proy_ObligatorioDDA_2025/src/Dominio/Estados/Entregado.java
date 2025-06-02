/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Pedido;

/**
 *
 * @author ianco
 */
public class Entregado {
    private Pedido pedido;

    public Entregado(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() {
        System.out.println("Ya fue confirmado.");
    }

    public void desconfirmar() {
        System.out.println("No se puede desconfirmar un pedido entregado.");
    }

    public void procesar() {
        System.out.println("Ya fue procesado.");
    }

    public void entregar() {
        System.out.println("Ya fue entregado.");
    }

    public void finalizar() {
        System.out.println("Pedido finalizado.");
        pedido.setEstado(new Finalizado(pedido));
    }

    public void validarEliminacion() throws ServicioException {
        throw new ServicioException("No se puede eliminar un pedido entregado.");
    }
}

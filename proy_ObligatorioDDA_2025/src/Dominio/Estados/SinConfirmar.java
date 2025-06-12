/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.StockException;
import Dominio.Ingrediente;
import Dominio.Insumo;
import Dominio.Observer.Observable;
import Dominio.Pedido;
import java.util.Collections;
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

    
 
    public void confirmar(Pedido pedido) throws StockException {
        
        pedido.setEstado(new Confirmado(pedido));     
        
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
    public String toString() {
        return "Sin Confirmar";
    }

    @Override
    public List<Ingrediente> ingredientesParaConfirmar(Pedido pedido) {
        return pedido.getItem().getIngredientes();
    }

}

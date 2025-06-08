/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.List;

/**
 *
 * @author ianco
 */
public interface EstadoPedido {
    void confirmar(Pedido pedido)throws StockException;
    void procesar();
    void entregar();
    void finalizar();
    void validarEliminacion() throws ServicioException;

    List<Ingrediente> ingredientesParaConfirmar(Pedido pedido);
}

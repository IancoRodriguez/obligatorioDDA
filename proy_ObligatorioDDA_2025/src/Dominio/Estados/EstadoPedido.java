/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Pedido;
import java.util.List;

/**
 *
 * @author ianco
 */
public interface EstadoPedido {
    void confirmar();
    void desconfirmar();
    void procesar();
    void entregar();
    void finalizar();
    void validarEliminacion() throws ServicioException;


    public void agregarSiEsConfirmado(Pedido pedido, List<Pedido> pedidos, String nombreUP);
}

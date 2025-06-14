
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.Collections;
import java.util.List;


public class Entregado implements EstadoPedido{
    private Pedido pedido;

    public Entregado(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() throws ServicioException {
        throw new ServicioException("Ya fue confirmado.");
    }

    public void procesar() throws ServicioException {
        throw new ServicioException("Ya fue procesado.");
    }

    public void entregar() throws ServicioException {
        throw new ServicioException("Pedido ya fue entregado");
    }

    public void finalizar() {
        pedido.setEstado(new Finalizado(pedido));
    }

    public void validarEliminacion() throws ServicioException {
        throw new ServicioException("No se puede eliminar un pedido entregado.");
    }

    @Override
    public String toString() {
        return "Entregado";
    }

    @Override
    public List<Ingrediente> ingredientesParaConfirmar(Pedido pedido) {
       return Collections.emptyList();
    }
    
    
}

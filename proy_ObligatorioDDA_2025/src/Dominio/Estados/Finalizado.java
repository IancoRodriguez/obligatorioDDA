
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.Collections;
import java.util.List;


public class Finalizado implements EstadoPedido{
    private Pedido pedido;

    public Finalizado(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() throws ServicioException {
        throw new ServicioException("Ya finalizado. No se puede confirmar.");
    }

    public void procesar() throws ServicioException {
        throw new ServicioException("Ya fue procesado.");
    }

    public void entregar() throws ServicioException {
        pedido.setEstado(new Entregado(pedido));
    }

    public void finalizar() throws ServicioException {
        throw new ServicioException("Ya está finalizado.");
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

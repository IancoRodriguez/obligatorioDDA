
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.List;

public class Procesando implements EstadoPedido {

    private Pedido pedido;

    public Procesando(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() throws StockException, ServicioException  {
        throw new ServicioException("Ya fue confirmado.");
    }

    public void procesar() throws ServicioException {
        throw new ServicioException("Pedido en proceso");
    }

    public void entregar() {
        pedido.setEstado(new Entregado(pedido));
    }

    public void finalizar() throws ServicioException {
        throw new ServicioException("No se puede finalizar sin entregar.");
    }

    public void validarEliminacion() throws ServicioException {
        throw new ServicioException("Un poco tarde…Ya estamos elaborando este pedido!");
    }

    @Override
    public String toString() {
        return "Procesando";
    }

    @Override
    public List<Ingrediente> ingredientesParaConfirmar(Pedido pedido) {
        return null;
    }

    
}

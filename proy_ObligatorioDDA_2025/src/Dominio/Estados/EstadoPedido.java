
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.List;


public interface EstadoPedido {
    void confirmar() throws StockException, ServicioException;
    void procesar() throws ServicioException;
    void entregar() throws ServicioException;
    void finalizar() throws ServicioException;
    void validarEliminacion() throws ServicioException;

    List<Ingrediente> ingredientesParaConfirmar(Pedido pedido);
}

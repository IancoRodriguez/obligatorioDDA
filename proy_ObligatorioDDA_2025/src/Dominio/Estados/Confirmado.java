
package Dominio.Estados;

import Dominio.Excepciones.ServicioException;
import Dominio.Ingrediente;
import Dominio.Pedido;
import java.util.Collections;
import java.util.List;


public class Confirmado implements EstadoPedido {
    private Pedido pedido;

    public Confirmado(Pedido pedido) {
        this.pedido = pedido;
    }

    public void confirmar() throws ServicioException {
        throw new ServicioException("El pedido ya está confirmado");
    }

    public void procesar() {
        pedido.setEstado(new Procesando(pedido));
    }

    public void entregar() throws ServicioException {
        throw new ServicioException("No se puede entregar sin procesar.");
    }

    public void finalizar() throws ServicioException {
        throw new ServicioException("No se puede finalizar sin entregar.");
    }

    public void validarEliminacion() {
  
    }
     
    @Override
    public String toString() {
        return "Confirmado";
    }

 
    @Override
    public List<Ingrediente> ingredientesParaConfirmar(Pedido pedido) {
        return Collections.emptyList();
    }
    
    

    
}


package UI.Controladores;

import Dominio.Gestor;
import Dominio.Observer.Observable;
import Dominio.Pedido;
import java.util.List;

public interface GestorView {
        
    void cargarNombreUP(String nombreGestor, String nombreUP);
    void cargarPedidosPendientesUP(List<Pedido> pedidosPendientes);
    void cargarPedidosTomados(List<Pedido> pedidosTomadosPorGestor);
    void tomarPedido();
    void entregarPedido();
    void finalizarPedido();
    
}

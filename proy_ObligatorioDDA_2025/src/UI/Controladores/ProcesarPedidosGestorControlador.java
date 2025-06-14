
package UI.Controladores;

import Dominio.Gestor;
import Dominio.Observer.Observable;
import Dominio.Observer.Observador;
import Dominio.Pedido;
import Dominio.Servicio;
import Servicios.Fachada;


public class ProcesarPedidosGestorControlador implements Observador {
    
    private Gestor gestor; 
    private Fachada f;
    private GestorView vistaGestor;
    
    public ProcesarPedidosGestorControlador(Gestor g, GestorView vista){
        
        this.gestor = g;
        this.vistaGestor = vista;
        this.f = Fachada.getInstancia();
        this.vistaGestor.cargarNombreUP(gestor.getNombreCompleto(), gestor.getNombreUP());
        
        suscribirAServicios();
        f.subscribir(this);
        
        this.vistaGestor.cargarPedidosPendientesUP(f.getPedidosConfirmados(gestor.getUP().getNombre()));
        
    }

    @Override
    public void notificar(Observable origen, Object evento) {
        
        if (evento instanceof Observable.Evento && evento == Observable.Evento.NUEVO_SERVICIO) {
            suscribirAServicios();
        }
        
        if (evento instanceof Observable.Evento && evento == Observable.Evento.PEDIDO_CAMBIO_ESTADO) {
            cargarPedidosPendientesUP();
        }
                
    }
    
    public void suscribirAServicios(){
        for(Servicio s : f.getServicios()){
            s.desuscribir(this);
            s.subscribir(this);
        }
    }

    private void cargarPedidosPendientesUP() {
        
        this.vistaGestor.cargarPedidosPendientesUP(f.getPedidosConfirmados(gestor.getUP().getNombre()));
                
    }
    
    public void tomarPedido(Pedido p){
        f.tomarPedido(this.gestor, p);     
        
        cargarPedidosPendientesUP();
        cargarPedidosTomados();
    }
    
    public void entregarPedido(int posPedido){
        if(posPedido >= 0){
            Pedido p = this.gestor.getPedidosTomados().get(posPedido);
            p.entregar();
            cargarPedidosTomados();
        }
    }
    
    public void finalizarPedidio(int posPedido){
        if(posPedido >= 0){
            Pedido p = this.gestor.getPedidosTomados().get(posPedido);
            p.finalizar();
            cargarPedidosTomados();
        }
    }
    
    private void cargarPedidosTomados() {
        this.vistaGestor.cargarPedidosTomados(this.gestor.getPedidosTomados());
    }
    
}

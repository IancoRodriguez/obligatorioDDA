
package Servicios;

import Dominio.Dispositivo;
import Dominio.Estados.Confirmado;
import Dominio.Pedido;
import Dominio.Servicio;
import java.util.ArrayList;
import java.util.List;


public class ServicioDispositivos {

    private List<Dispositivo> dispositivos;

    public ServicioDispositivos() {
        this.dispositivos = new ArrayList();
    }

    public List<Pedido> getTodosLosPedidos() {
        List<Pedido> todosLosPedidos = new ArrayList();

        for (Dispositivo d : dispositivos) {
            if(d.getServicioActivo() != null ){
                todosLosPedidos.addAll(d.getServicioActivo().getPedidos());
            }
            
        }

        return todosLosPedidos;
    }

    public List<Pedido> getPedidosConfirmados(String nombreUP) {
        
        List<Pedido> pedidos = new ArrayList<>();
        
        for(Servicio s : getServicios() ){
            for (Pedido pedido : s.getPedidos()) {
                if(pedido.getEstado()  instanceof Confirmado && pedido.getItem().getUnidadProcesadora().getNombre() == nombreUP){
                    pedidos.add(pedido);
                } 
            }          
        }
        return pedidos;
    }

    public boolean agregar(Dispositivo d) {
        dispositivos.add(d);
        return true;
    }

    //Getters y Setters 
    public List<Dispositivo> getDispositivos() {
        return dispositivos;
    }

    public List<Servicio> getServicios() {
        
        List<Servicio> servicios = new ArrayList();
        
        for (Dispositivo d : dispositivos) {
            if(d.getServicioActivo() != null ){
                servicios.add(d.getServicioActivo());
            }
        }
        return servicios;
    }
    
    

}

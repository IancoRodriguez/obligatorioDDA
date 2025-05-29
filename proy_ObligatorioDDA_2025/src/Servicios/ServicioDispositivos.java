/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Item;
import Dominio.Pedido;
import Dominio.Servicio;
import Dominio.UnidadProcesadora;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuar
 */
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

    public boolean agregar(Dispositivo d) {
        dispositivos.add(d);
        return true;
    }

    //Getters y Setters 
    public List<Dispositivo> getDispositivos() {
        return dispositivos;
    }

    public List<Servicio> getServiciosDeLaUP(UnidadProcesadora up) {
        
        List<Servicio> servicios = new ArrayList();
        
        
        for (Dispositivo d : dispositivos) {
            if(d.getServicioActivo() != null ){
                if(!d.getServicioActivo().getItemsPorUP(up).isEmpty()){
                    servicios.add(d.getServicioActivo());
                }
            }
        }
        return servicios;
    }
    
    

}

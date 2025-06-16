package Dominio;

import Dominio.Excepciones.DispositivoException;

public class Dispositivo {
    private Servicio servicioActivo; 
    private final int id;

    
    private static final class IdCounter { 
        private int nextId = 1;
    }

    private static final IdCounter counter = new IdCounter();

    public Dispositivo() {
        synchronized (IdCounter.class) { 
            this.id = counter.nextId++;
        }
    }
    
    public Cliente clienteLogueado(){
        return servicioActivo.getCliente();
    }

    public int getId() {
        return id;
    }
    
    
   

    public void liberar() {
        this.servicioActivo = null;
    }

    public boolean estaOcupado() {
        return servicioActivo != null;
    }

    public void setServicioActivo(Servicio servicio) throws DispositivoException {
        /* if (this.servicioActivo != null) {
            throw new DispositivoException("Dispositivo ya ocupado");
        }*/
        this.servicioActivo = servicio;
    }
    
    
    
    
    
    
    
    
    

    // Getter para acceder al servicio activo desde otras clases
    public Servicio getServicioActivo() {
        return servicioActivo;
    }
    
    
}

package Dominio;

public class Dispositivo {
    private Servicio servicioActivo; 
    private final int id;

    
    private static final class IdCounter { // Clase interna estática (no cuenta como "clase extra")
        private int nextId = 1;
    }

    // Instancia única del contador (encapsulada dentro de Dispositivo)
    private static final IdCounter counter = new IdCounter();

    public Dispositivo() {
        synchronized (IdCounter.class) { // Sincronización para thread-safe
            this.id = counter.nextId++;
        }
    }

    public int getId() {
        return id;
    }
    
    
    public void ocupar(Servicio servicio) {
        if (this.servicioActivo != null) {
            throw new IllegalStateException("Dispositivo ya ocupado");
        }
        this.servicioActivo = servicio;
    }

    public void liberar() {
        this.servicioActivo = null;
    }

    public boolean estaOcupado() {
        return servicioActivo != null;
    }
    
    
    
    
    
    
    
    
    

    // Getter para acceder al servicio activo desde otras clases
    public Servicio getServicioActivo() {
        return servicioActivo;
    }
    
    
}

package Dominio;

public class UnidadProcesadora {

    private String nombre;

    public UnidadProcesadora(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la unidad procesadora no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    // Getter
    public String getNombre() {
        return nombre;
    }

    // Setter 
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la unidad procesadora no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}


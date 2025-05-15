package Dominio;

public class UnidadProcesadora {

    private String nombre;

    // Constructor con validación
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

    // Setter (opcional, si querés que se pueda cambiar el nombre)
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la unidad procesadora no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    // Para mostrar en la UI (por ejemplo, en JList o JComboBox)
    @Override
    public String toString() {
        return nombre;
    }
}


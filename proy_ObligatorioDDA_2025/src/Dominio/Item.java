package Dominio;

import java.util.List;

public class Item {

    private String Nombre;
    private float precioUnitario;
    private UnidadProcesadora unidadProcesadora;
    private List<Ingrediente> ingredientes;

    public Item(String Nombre, float precioUnitario,UnidadProcesadora unidadProcesadora, List<Ingrediente> ingredientes) {

        // Validaciones 
        if (Nombre == null || Nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (precioUnitario < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (unidadProcesadora == null) {
            throw new IllegalArgumentException("La unidad procesadora es obligatoria");
        }

        // Inicialización de atributos
        this.Nombre = Nombre;
        this.precioUnitario = precioUnitario;
        this.unidadProcesadora = unidadProcesadora;

        
    }
}

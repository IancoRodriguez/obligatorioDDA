package Dominio;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private String Nombre;
    private float precioUnitario;
    private UnidadProcesadora unidadProcesadora;
    private List<Ingrediente> ingredientes;
    private Categoria categoria;
    
    public Item(String Nombre, float precioUnitario,UnidadProcesadora unidadProcesadora, Categoria categoria) {

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
        if (categoria == null) { // Validación nueva para categoría
            throw new IllegalArgumentException("La categoría es obligatoria");
        }

        // Inicialización de atributos
        this.Nombre = Nombre;
        this.precioUnitario = precioUnitario;
        this.unidadProcesadora = unidadProcesadora;
        this.ingredientes = new ArrayList();
        this.categoria = categoria;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public UnidadProcesadora getUnidadProcesadora() {
        return unidadProcesadora;
    }

    public void setUnidadProcesadora(UnidadProcesadora unidadProcesadora) {
        this.unidadProcesadora = unidadProcesadora;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
        this.ingredientes = ingredientes;
    }
    
    
    public void agregarIngrediente(Ingrediente ingrediente) {
        ingredientes.add(ingrediente);
    }
    
    
    public boolean tieneStockDisponible() {
        for (Ingrediente ingrediente : ingredientes) {
            if (!ingrediente.tieneStockSuficiente()) {
                return false; // El ítem valida todos sus ingredientes
            }
        }
        return true;
    }

    public Object getCategoria() {
        return this.categoria;
    }
    
}

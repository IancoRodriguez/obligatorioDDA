package Dominio;

import java.util.ArrayList;
import java.util.List;

public class Categoria {

    private String nombre;
    private List<Item> items;

    public Categoria(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo ni vacío");
        }
        this.nombre = nombre;
        this.items = new ArrayList<>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public List<Item> getItems() {
        return items;
    }

    // Agregar un ítem
    public void agregarItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("El ítem no puede ser nulo");
        }
        items.add(item);
    }

    // Eliminar un ítem
    public void eliminarItem(Item item) {
        items.remove(item);
    }
    
 
    

    // Representación legible
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(nombre + ":\n");
        for (Item i : items) {
            sb.append("  - ").append(i.getNombre()).append(" ($").append(i.getPrecioUnitario()).append(")\n");
        }
        return sb.toString();
    }
}

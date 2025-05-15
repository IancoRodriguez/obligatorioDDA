package Dominio;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private String nombre;
    private List<Categoria> categorias;

    public Menu() {
        this.categorias = new ArrayList<>();
    }
     public Menu(String nombre) {
        this();
        this.nombre = nombre;
    }

    // Getter
    public List<Categoria> getCategorias() {
        return categorias;
    }

    // Agregar una categoría
    public void agregarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }
        categorias.add(categoria);
    }

    // Eliminar una categoría
    public void eliminarCategoria(Categoria categoria) {
        categorias.remove(categoria);
    }
    
     public Categoria buscarCategoriaPorNombre(String nombreCategoria) {
        for (Categoria categoria : categorias) {
            if (categoria.getNombre().equals(nombreCategoria)) {
                return categoria;
            }
        }
        return null; // No se encontró
    }

    // Representación legible
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Menú:\n");
        for (Categoria c : categorias) {
            sb.append("- ").append(c.getNombre()).append("\n");
        }
        return sb.toString();
    }
}

package Dominio;

import Dominio.Observer.Observable;
import Dominio.Observer.Observador;
import java.util.ArrayList;
import java.util.List;

public class Item extends Observable {
    private String nombre;
    private String descripcion;
    private float precioUnitario;
    private UnidadProcesadora unidadProcesadora;
    private List<Ingrediente> ingredientes;
    private Categoria categoria;
    
    // Constructor que coincide con tu carga de datos
    public Item(String nombre, float precio, UnidadProcesadora unidadProcesadora, Categoria categoria) {
        this.nombre = nombre;
        this.precioUnitario = precio;
        this.unidadProcesadora = unidadProcesadora;
        this.categoria = categoria;
        this.ingredientes = new ArrayList<>();
        this.descripcion = ""; // valor por defecto
    }
    
    // Constructor completo (mantener por compatibilidad)
    public Item(String nombre, String descripcion, float precio, 
                UnidadProcesadora unidadProcesadora, List<Ingrediente> ingredientes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioUnitario = precio;
        this.unidadProcesadora = unidadProcesadora;
        this.ingredientes = ingredientes;
    }
    
    public boolean tieneStockDisponible() {
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getInsumo().getStock() < ingrediente.getCantidad()) {
                return false;
            }
        }
        return true;
    }
    
    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public float getPrecioUnitario() {
        return precioUnitario;
    }
    
    public void setPrecioUnitario(float precio) {
        this.precioUnitario = precio;
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
        this.ingredientes.add(ingrediente);
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public String toString() {
        return nombre + " - $" + precioUnitario;
    }
}
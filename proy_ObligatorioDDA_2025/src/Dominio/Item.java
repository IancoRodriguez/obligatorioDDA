package Dominio;

import Dominio.Observer.Observable;
import Dominio.Observer.Observador;
import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream
public class Item extends Observable {
    private String nombre;
    private String descripcion;
=======
public class Item extends Observable implements Observador {
    
    private String nombre;
>>>>>>> Stashed changes
    private float precioUnitario;
    private List<Ingrediente> ingredientes;
<<<<<<< Updated upstream
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
=======
    private UnidadProcesadora unidadProcesadora;

    public Item(String nombre, float precioUnitario, List<Ingrediente> ingredientes, UnidadProcesadora unidadProcesadora) {
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.ingredientes = ingredientes;
        this.unidadProcesadora = unidadProcesadora;
        
        // NUEVO: Suscribirse a todos los insumos que usa este item
        suscribirseAInsumos();
    }
    
    // NUEVO: Método para suscribirse a los insumos
    private void suscribirseAInsumos() {
        if (ingredientes != null) {
            for (Ingrediente ingrediente : ingredientes) {
                ingrediente.getInsumo().subscribir(this);
            }
        }
    }

    public boolean tieneStockDisponible() {
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.getInsumo().getStock() < ingrediente.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    // NUEVO: Implementación del Observer para reaccionar a cambios en insumos
    @Override
    public void notificar(Observable origen, Object evento) {
        if (evento instanceof Observable.Evento) {
            Observable.Evento tipoEvento = (Observable.Evento) evento;
            
            switch (tipoEvento) {
                case STOCK_ACTUALIZADO:
                    // MODIFICADO: Verificar si el item perdió disponibilidad
                    if (!tieneStockDisponible()) {
                        // Notificar que este item se quedó sin stock
                        notificar(Evento.ITEM_SIN_STOCK);
                    } else {
                        // Notificar que el item cambió (podría haber recuperado stock)
                        notificar(Evento.ITEM_ACTUALIZADO);
                    }
                    break;
                    
                case STOCK_AGOTADO:
                    // Si se agotó el stock de un insumo que necesitamos, 
                    // verificar si este item ya no tiene stock disponible
                    if (!tieneStockDisponible()) {
                        // Notificar que este item se quedó sin stock
                        notificar(Evento.ITEM_SIN_STOCK);
                    }
                    break;
            }
        }
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public float getPrecioUnitario() {
        return precioUnitario;
    }

    public List<Ingrediente> getIngredientes() {
        return ingredientes;
    }

    public UnidadProcesadora getUnidadProcesadora() {
        return unidadProcesadora;
    }

    // Método para agregar un ingrediente
    public void agregarIngrediente(Ingrediente ingrediente) {
        if (this.ingredientes == null) {
            this.ingredientes = new ArrayList<>();
        }
        this.ingredientes.add(ingrediente);
        // Suscribirse al nuevo insumo
        ingrediente.getInsumo().subscribir(this);
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioUnitario(float precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setIngredientes(List<Ingrediente> ingredientes) {
>>>>>>> Stashed changes
        this.ingredientes = ingredientes;
        // Re-suscribirse a todos los insumos cuando se cambia la lista
        suscribirseAInsumos();
    }
<<<<<<< Updated upstream
    
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
<<<<<<< Updated upstream
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
=======

    public void setUnidadProcesadora(UnidadProcesadora unidadProcesadora) {
        this.unidadProcesadora = unidadProcesadora;
=======
        return this.categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void verificarDisponibilidad() {
    boolean antes = disponible;
    disponible = true;
    for (Ingrediente ing : ingredientes) {
        if (!ing.tieneStockSuficiente()) {
            disponible = false;
            break;
        }
>>>>>>> Stashed changes
    }
    
    // Si cambió de disponible a no disponible, notificar
    if (antes && !disponible) {
        notificar(Observable.Evento.ITEM_SIN_STOCK);
    }
    
    // Notificar actualización general
    if (disponible != antes) {
        notificar(Observable.Evento.ITEM_ACTUALIZADO);
    }
}

>>>>>>> Stashed changes
    @Override
    public String toString() {
        return nombre + " - $" + precioUnitario;
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes

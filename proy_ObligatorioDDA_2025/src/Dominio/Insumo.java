package Dominio;

import Dominio.Observer.Observable;
import Dominio.Excepciones.StockException;

public class Insumo extends Observable{

    private String nombre;
    private int stock;
    private int stockMinimo;

    // Constructor con validaciones
    public Insumo(String nombre, int stock, int stockMinimo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }

        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    // Setters con validaciones
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        this.stockMinimo = stockMinimo;
    }

    // Método para agregar stock
    public void agregarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        stock += cantidad;
        notificar(Evento.STOCK_ACTUALIZADO);
    }

    // Método para consumir stock (con validación de stock mínimo)
    public void consumirStock(int cantidad) throws StockException {
        if (cantidad < 0) {
            throw new StockException("La cantidad no puede ser negativa");
        }
        stock -= cantidad;
        // Notificar siempre que cambia stock
        notificar(Evento.STOCK_ACTUALIZADO);
        // Si cae por debajo del mínimo, podrías notificar STOCK_INSUFICIENTE
        if (stock < stockMinimo) {
            notificar(Evento.STOCK_INSUFICIENTE);
        }
 
    }

    
    
    
    
    
    
    // Representación legible del objeto
    @Override
    public String toString() {
        return String.format(
                "Insumo: %s | Stock: %d | Stock mínimo: %d",
                nombre, stock, stockMinimo
        );
    }

}

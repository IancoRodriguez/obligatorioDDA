package Dominio;

import Dominio.Observer.Observable;
import Dominio.Excepciones.StockException;
import java.util.ArrayList;
import java.util.List;

public class Insumo extends Observable {
    private String nombre;
    private int stock;
    private int stockMinimo; // Manteniendo el campo que tenías
    
    // Constructor que coincide con tu carga de datos
    public Insumo(String nombre, int stock, int stockMinimo) {
        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
    }
    
    // Constructor alternativo si necesitas solo nombre y stock
    public Insumo(String nombre, int stock) {
        this.nombre = nombre;
        this.stock = stock;
        this.stockMinimo = 0; // valor por defecto
    }
    
    public void consumirStock(int cantidad) throws StockException {
        if (stock < cantidad) {
            throw new StockException("Stock insuficiente de " + nombre);
        }
        
        int stockAnterior = this.stock;
        this.stock -= cantidad;
        
        // Si el stock llegó a 0, notificar para que los servicios
        // eliminen pedidos pendientes de este insumo
        if (stockAnterior > 0 && this.stock == 0) {
            notificar(Evento.INSUMO_AGOTADO);
        } else {
            // Notificación normal de cambio de stock
            notificar(Evento.STOCK_ACTUALIZADO);
        }
    }
    
    public void agregarStock(int cantidad) {
        this.stock += cantidad;
        notificar(Evento.STOCK_ACTUALIZADO);
    }
    
    // Getters y setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public int getStockMinimo() {
        return stockMinimo;
    }
    
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }
    
    @Override
    public String toString() {
        return nombre + " (Stock: " + stock + ")";
    }
}
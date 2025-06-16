package Dominio;

import java.util.Objects;

public class Ingrediente  {

    private final Insumo insumo;  
    private int cantidad;         

    public Ingrediente(Insumo insumo, int cantidad) {
        if (insumo == null) {
            throw new IllegalArgumentException("El insumo no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.insumo = insumo;
        this.cantidad = cantidad;
        
    }

    // Getters
    public Insumo getInsumo() {
        return insumo;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Setters
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return String.format(
                "%s - Cantidad: %d",
                insumo.getNombre(),
                cantidad
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ingrediente that = (Ingrediente) o;
        return cantidad == that.cantidad && insumo.equals(that.insumo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(insumo, cantidad);
    }
    
    
    
    public boolean tieneStockSuficiente(){
        return insumo.getStock() >= cantidad;
    }

   
}

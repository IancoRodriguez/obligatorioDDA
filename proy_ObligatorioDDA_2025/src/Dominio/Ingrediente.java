package Dominio;

import java.util.Objects;

public class Ingrediente {

    private final Insumo insumo;  // El insumo no cambia luego de la creación
    private int cantidad;         // La cantidad sí puede modificarse con validación

    // Constructor con validaciones
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

    // Setter para cantidad con validación
    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        this.cantidad = cantidad;
    }

    // Representación legible del objeto
    @Override
    public String toString() {
        return String.format(
                "%s - Cantidad: %d",
                insumo.getNombre(),
                cantidad
        );
    }

    // Implementación de equals() y hashCode()
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
}

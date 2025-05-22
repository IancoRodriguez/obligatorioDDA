package Dominio.Tipos;

import Dominio.Pedido;
import java.util.List;

public abstract class TipoCliente {
    private String nombre;

    public TipoCliente(String nombre) {
        this.nombre = nombre;
    }
    
    public abstract double aplicarBeneficio(List<Pedido> pedidos, double montoTotal);
    public abstract String getMensajeBeneficio();

}

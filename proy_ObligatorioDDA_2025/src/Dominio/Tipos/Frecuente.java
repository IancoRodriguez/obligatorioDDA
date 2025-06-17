
package Dominio.Tipos;

import Dominio.Pedido;
import java.util.List;


public class Frecuente extends TipoCliente {

    public Frecuente(String nombre) {
        super(nombre);
    }

    @Override
    public double aplicarBeneficio(List<Pedido> pedidos, double montoTotal) {
        double descuento = 0;

        // Sumar el precio de todos los cafés
        for (Pedido p : pedidos) {
            if (p.getItem().getCategoria().getNombre().equals("Cafés")) {
                descuento += p.getItem().getPrecioUnitario();
            }
        }

        return montoTotal - descuento;
    }

    @Override
    public String getMensajeBeneficio() {
        return "Cafés de cortesía";
    }

}

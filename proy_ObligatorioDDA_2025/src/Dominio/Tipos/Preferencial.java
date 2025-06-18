
package Dominio.Tipos;

import Dominio.Pedido;
import java.util.List;

public class Preferencial extends TipoCliente {

    public Preferencial(String nombre) {
        super(nombre);
    }

    @Override
    public double aplicarBeneficio(List<Pedido> pedidos, double montoTotal) {
        double descuentoAguas = 0;
        double descuentoTotal = 0;

        // Calcular descuento por aguas
        for (Pedido pedido : pedidos) {
            if (pedido.getItem().getNombre().equals("Agua Mineral")) {
                descuentoAguas += pedido.getItem().getPrecioUnitario();
            }
        }

        // Aplicar 5% sobre el total original si supera 2000
        if (montoTotal > 2000) {
            montoTotal -= descuentoAguas;
            descuentoTotal = montoTotal * 0.05;
        }

        return montoTotal - descuentoTotal;
    }

    @Override
    public String getMensajeBeneficio() {
        return "Agua Mineral gratis + 5% de descuento";
    }

}

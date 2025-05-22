/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Tipos;

import Dominio.Pedido;
import Dominio.Tipos.TipoCliente;
import java.util.List;

/**
 *
 * @author usuar
 */
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

        // Aplicar 5% sobre el total original si supera $2000
        if (montoTotal > 2000) {
            descuentoTotal = montoTotal * 0.05;
        }

        return (montoTotal - descuentoAguas) - descuentoTotal;
    }

    @Override
    public String getMensajeBeneficio() {
        return "Agua Mineral gratis + 5% de descuento";
    }

}

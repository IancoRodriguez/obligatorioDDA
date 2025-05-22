/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Tipos;

import Dominio.Pedido;
import Dominio.Tipos.TipoCliente;
import java.util.List;
import Dominio.Categoria;

/**
 *
 * @author usuar
 */
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

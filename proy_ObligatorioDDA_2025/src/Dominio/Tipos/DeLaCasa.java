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
public class DeLaCasa extends TipoCliente {

    public DeLaCasa(String nombre) {
        super(nombre);
    }

    @Override
    public double aplicarBeneficio(List<Pedido> pedidos, double montoTotal) {
        // para que el monto nunca sea negativo
        return Math.max(montoTotal - 500, 0);
    }

    @Override
    public String getMensajeBeneficio() {
        return "$500 de consumo gratis";
    }

}

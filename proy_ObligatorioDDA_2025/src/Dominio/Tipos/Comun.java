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
public class Comun extends TipoCliente {
    
    public Comun(String nombre) {
        super(nombre);
    }

    @Override
    public double aplicarBeneficio(List<Pedido> pedidos, double montoTotal) {
                return montoTotal; // Sin cambios
    }

    @Override
    public String getMensajeBeneficio() {
        return "";
    }
    
}

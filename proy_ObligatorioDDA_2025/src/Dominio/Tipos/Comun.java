
package Dominio.Tipos;

import Dominio.Pedido;
import Dominio.Tipos.TipoCliente;
import java.util.List;


public class Comun extends TipoCliente {
    
    public Comun(String nombre) {
        super(nombre);
    }

    @Override
    public double aplicarBeneficio(List<Pedido> pedidos, double montoTotal) {
                return montoTotal; 
    }

    @Override
    public String getMensajeBeneficio() {
        return "";
    }
    
}

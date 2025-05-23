/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Gestor;
import Dominio.Pedido;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bruno
 */
public class ServicioGestor {
    
    private List<Gestor> gestores;

    public ServicioGestor() {
        this.gestores = new ArrayList<Gestor>();
    }   

    public void tomarPedido(Gestor gestor, Pedido p) {
        gestor.setPedidosTomados(p);
    }
    
    
    
}

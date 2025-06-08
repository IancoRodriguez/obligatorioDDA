/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio.Observer;

import Dominio.Observer.Observador;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ianco
 */
public abstract class Observable {
    private List<Observador> subscriptores;
    public enum Evento {ITEM_ACTUALIZADO, STOCK_ACTUALIZADO, STOCK_INSUFICIENTE, MONTO_ACTUALIZADO, PEDIDO_CONFIRMADO}

    public Observable() {
        this.subscriptores = new ArrayList();
    }
    
    public void subscribir(Observador subscriptor){
        this.subscriptores.add(subscriptor);
    }
    
    public void desuscribir(Observador subscriptor){
        this.subscriptores.remove(subscriptor);
    }
    
    public void notificar(Object evento){
        for(Observador o : subscriptores){
            o.notificar(this , evento);
        }
    }
    
    
}

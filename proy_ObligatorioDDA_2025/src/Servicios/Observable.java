/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import java.util.List;

/**
 *
 * @author ianco
 */
public abstract class Observable {
    private List<Observador> subscriptores;
    public enum Evento {STOCK_INSUFICIENTE}

    public Observable() {
        this.subscriptores = subscriptores;
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

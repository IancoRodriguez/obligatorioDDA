/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ianco
 */
public abstract class Observable {
    private List<Observador> subscriptores;
    public enum Evento {STOCK_ACTUALIZADO, STOCK_RECUPERADO, STOCK_INSUFICIENTE, INTENTO_CONSUMO_INVALIDO, ITEM_ACTUALIZADO, INGREDIENTE_ACTUALIZADO, CATEGORIA_ACTUALIZADA}

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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agenda.servicios;

import Servicios.Observable;

/**
 *
 * @author dgreg
 */
public interface Observador {
    public void notificar(Observable origen, Object evento);
}

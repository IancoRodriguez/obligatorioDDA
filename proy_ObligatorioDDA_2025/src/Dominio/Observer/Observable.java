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

    public enum Evento {
        ITEM_ACTUALIZADO, STOCK_ACTUALIZADO, STOCK_INSUFICIENTE, MONTO_ACTUALIZADO, PEDIDO_CAMBIO_ESTADO,
        NUEVO_SERVICIO, PEDIDOS_ELIMINADOS_POR_STOCK, PEDIDOS_CONFIRMADOS
    }

    public Observable() {
        this.subscriptores = new ArrayList();
    }

    public void subscribir(Observador subscriptor) {
        this.subscriptores.add(subscriptor);
    }

    public void desuscribir(Observador subscriptor) {
        this.subscriptores.remove(subscriptor);
    }

    public void notificar(Object evento) {
        for (Observador o : subscriptores) {
            o.notificar(this, evento);
        }
    }

    // Método sobrecargado para casos donde necesitas enviar datos adicionales
    public void notificar(Evento tipoEvento, Object datos) {
        EventoConDatos eventoCompleto = new EventoConDatos(tipoEvento, datos);
        notificar(eventoCompleto);
    }

    // Clase interna para encapsular evento + datos
    public static class EventoConDatos {
        private final Evento tipo;
        private final Object datos;

        public EventoConDatos(Evento tipo, Object datos) {
            this.tipo = tipo;
            this.datos = datos;
        }

        public Evento getTipo() {
            return tipo;
        }

        public Object getDatos() {
            return datos;
        }

        @SuppressWarnings("unchecked")
        public <T> T getDatos(Class<T> tipoEsperado) {
            if (datos != null && tipoEsperado.isAssignableFrom(datos.getClass())) {
                return (T) datos;
            }
            return null;
        }
    }
}

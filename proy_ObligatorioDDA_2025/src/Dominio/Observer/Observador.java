package Dominio.Observer;

public interface Observador {
    public void notificar(Observable origen, Object evento);
 
}

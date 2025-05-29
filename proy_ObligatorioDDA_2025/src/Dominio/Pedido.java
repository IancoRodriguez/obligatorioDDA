package Dominio;

import Dominio.Excepciones.StockException;
import java.time.LocalDate;

public class Pedido {
    private String comentario;
    private Gestor gestor;
    private Item item;
    private String estado;
    private LocalDate fechaHora;

    


    public Pedido(Item item, String comentario) throws StockException {
        this.item = item;
        this.comentario = comentario != null ? comentario : ""; // Comentario opcional
        this.estado = "No confirmado";
        this.validar() ;
        this.fechaHora = LocalDate.now();
    }
    
    public LocalDate getFechaHora() {
        return fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    
    
    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Gestor getGestor() {
        return gestor;
    }

    public void setGestor(Gestor gestor) {
        this.gestor = gestor;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    
    public float calcularTotal(){
        return this.item.getPrecioUnitario();
    }
    
    
    public void validar() throws StockException{
        validarStockItem();
    }
    
    private void validarStockItem() throws StockException {
        if(!item.tieneStockDisponible()){
            throw new StockException("Sin stock pa");
        }
    }

        

}

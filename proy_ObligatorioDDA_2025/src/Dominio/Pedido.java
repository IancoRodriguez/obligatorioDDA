package Dominio;

import Dominio.Excepciones.StockException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private String comentario;
    private Gestor gestor;
    private Item item;
    private String estado;
    private String fechaHora;

    


    public Pedido(Item item, String comentario) throws StockException {
        this.item = item;
        this.comentario = comentario != null ? comentario : ""; // Comentario opcional
        this.estado = "No confirmado";
        this.fechaHora = fechaHora();
        this.validar() ;
        
    }
    
    public String getFechaHora() {
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
    
    private String fechaHora(){
        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String fechaHoraFormateada = fechaHora.format(formato);
        
        return fechaHoraFormateada;
    }
        

}

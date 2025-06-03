package Dominio;

import Dominio.Estados.EstadoPedido;
import Dominio.Estados.SinConfirmar;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Pedido {
    private String comentario;
    private Gestor gestor;
    private Item item;
    private EstadoPedido estado;
    private String fechaHora;

    


    public Pedido(Item item, String comentario) throws StockException {
        this.item = item;
        this.comentario = comentario != null ? comentario : ""; // Comentario opcional
        this.fechaHora = fechaHora();
        this.validar() ;
        this.estado = new SinConfirmar(this);
        
    }
    
    public String getFechaHora() {
        return fechaHora;
    }      
   
    private String fechaHora(){
        LocalDateTime fechaHora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String fechaHoraFormateada = fechaHora.format(formato);
        
        return fechaHoraFormateada;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
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

    
    
    
    // Métodos delegados
    public void confirmar()      { estado.confirmar(); }
    public void desconfirmar()   { estado.desconfirmar(); }
    public void procesar()       { estado.procesar(); }
    public void entregar()       { estado.entregar(); }
    public void finalizar()      { estado.finalizar(); }
    public void validarEliminacion() throws ServicioException {estado.validarEliminacion();}

    
    
    
        

}

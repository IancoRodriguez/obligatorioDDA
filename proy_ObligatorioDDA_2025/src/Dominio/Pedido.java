package Dominio;

import Dominio.Excepciones.SinStockException;

public class Pedido {
    private String comentario;
    private Gestor gestor;
    private Item item;
    private String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public Pedido(Item item, String comentario) throws SinStockException {
        this.item = item;
        this.comentario = comentario != null ? comentario : ""; // Comentario opcional
        this.estado = "No confirmado";
        this.validar() ;
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
    
    
    public void validar() throws SinStockException{
        validarStockItem();
    }
    
    private void validarStockItem() throws SinStockException {
        if(!item.tieneStockDisponible()){
            throw new SinStockException("Sin stock pa");
        }
    }

        

}

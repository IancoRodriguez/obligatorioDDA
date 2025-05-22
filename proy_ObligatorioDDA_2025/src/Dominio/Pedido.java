package Dominio;

public class Pedido {
    private String comentario;
    private Gestor gestor;
    private Item item;

    public Pedido(String comentario, Item item) {
        this.comentario = comentario;
        this.item = item;
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

}

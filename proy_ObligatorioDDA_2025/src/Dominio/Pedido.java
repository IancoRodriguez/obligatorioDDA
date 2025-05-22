package Dominio;

public class Pedido {
    private String comentario;
    private Gestor gestor;
    private Item item;
    private Servicio servicio;
    private String estado;


    public Pedido(Item item, String comentario, Servicio servicio) {
        this.item = item;
        this.comentario = comentario != null ? comentario : ""; // Comentario opcional
        this.servicio = servicio;
        this.estado = "No confirmado";
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

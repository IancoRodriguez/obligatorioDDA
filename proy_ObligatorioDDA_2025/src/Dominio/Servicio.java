package Dominio;

import Dominio.Estados.Confirmado;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import java.util.ArrayList;
import java.util.List;

public class Servicio {
    private Cliente cliente;
    private List<Pedido> pedidos;
    private double montoTotal;

    public Servicio(Cliente cliente) {
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
    }

    // Agrega un pedido al servicio
    public void agregarPedido(Pedido pedido) throws ServicioException {
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
    }
    
    // Eliminar pedido del servicio 
    
    public void eliminarPedido(Pedido pedido) throws ServicioException{
        pedido.validarEliminacion();
        pedidos.remove(pedido);
        montoTotal -= pedido.calcularTotal();
    }
    
    

    // Confirma el servicio y valida el stock
    public void confirmar() throws StockException {
        try{
        validarStockPedidos();
        for(Pedido p : pedidos){
            p.confirmar();
            for(Ingrediente i : p.getItem().getIngredientes()){
                i.getInsumo().consumirStock(i.getCantidad());
            }
        }
        
        }catch(StockException e){
            System.out.println(e.getMessage());
        }
        
        
        //asignarUnidadesProcesadoras();
    }

    private void validarStockPedidos() throws StockException {
        for (Pedido pedido : pedidos) {
            if (!pedido.getItem().tieneStockDisponible()) {
                throw new StockException("Stock insuficiente para: " + pedido.getItem().getNombre());
            }
            
        }
    }
    // Finaliza el servicio y aplica beneficios
    public void finalizar() throws ServicioException {
        for (Pedido p : pedidos) {
            p.finalizar();
        }
        aplicarBeneficiosCliente();
        //estado = "Finalizado";
    }

    private void aplicarBeneficiosCliente() {
        
        montoTotal = cliente.getTipoCliente()
            .aplicarBeneficio(pedidos, montoTotal);
    }
    
    
    // ======================
    // Getters
    // ======================

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
    
    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

 
    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
    
    public List<Item> getItemsPorUP(UnidadProcesadora UP){
        
        List<Item> itemsDeLaUP = new ArrayList();
        for(Pedido p : pedidos){
            if(p.getItem().getUnidadProcesadora().equals(UP) ){
                itemsDeLaUP.add(p.getItem());
            }
        }
        
        return itemsDeLaUP;
    }
    
    
    public List<PedidoVO> mostrarPedidosPorUP(UnidadProcesadora UP) {
        
        List<PedidoVO> res = new ArrayList();
        
        for (Pedido pedido : pedidos) {
            
            if(pedido.getItem().getUnidadProcesadora().equals(UP)){
                PedidoVO pVO = new PedidoVO(pedido.getItem().getNombre(), 
                        this.cliente.getNombreCompleto(), 
                        pedido.getFechaHora(),
                        pedido.getComentario(),
                        pedido.getEstado());
                
                res.add(pVO);
            }
        }
        
        
    return res;
}
    
    
    
    
   
}




/*
private void asignarUnidadesProcesadoras() {
        for (Pedido pedido : pedidos) {
            String unidad = determinarUnidadProcesadora(pedido);
            pedido.setUnidadProcesadora(unidad);
            pedido.setEstado("Confirmado");
        }
    }

*/
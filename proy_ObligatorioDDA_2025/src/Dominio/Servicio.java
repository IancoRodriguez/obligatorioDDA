package Dominio;

import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.SinStockException;
import java.util.ArrayList;
import java.util.List;

public class Servicio {
    private Cliente cliente;
    private List<Pedido> pedidos;
    private String estado; // "En curso", "Confirmado", "Finalizado"
    private double montoTotal;

    public Servicio(Cliente cliente) {
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
        this.estado = "En curso";
    }

    // Agrega un pedido al servicio
    public void agregarPedido(Pedido pedido) throws ServicioException {
        if (pedido.getEstado() == "En curso") {
            throw new ServicioException("No se pueden agregar pedidos a un servicio finalizado.");
        }
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
    }
    
    // Eliminar pedido del servicio 
    
    public void eliminarPedido(Pedido pedido) throws ServicioException{
        if (pedido.getEstado() == "Confirmado") {
            throw new ServicioException("No se pueden eliminar pedidos a un servicio confirmado.");
        }
        pedidos.remove(pedido);
        montoTotal -= pedido.calcularTotal();
    }
    
    

    // Confirma el servicio y valida el stock
    public void confirmar() throws SinStockException {
        validarStockPedidos();
        for(Pedido p : pedidos){
            if(p.getEstado() == "No confirmado")
                p.setEstado("Confirmado");
        }
        //asignarUnidadesProcesadoras();
    }

    private void validarStockPedidos() throws SinStockException {
        for (Pedido pedido : pedidos) {
            if (!pedido.getItem().tieneStockDisponible()) {
                throw new SinStockException("Stock insuficiente para: " + pedido.getItem().getNombre());
            }
            
        }
    }
    // Finaliza el servicio y aplica beneficios
    public void finalizar() throws ServicioException {
        for (Pedido p : pedidos) {
            if (p.getEstado() != "Confirmado") {
                throw new ServicioException("El servicio debe estar entregado para finalizar.");
            }
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
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
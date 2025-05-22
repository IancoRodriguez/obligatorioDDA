package Dominio;

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
    public void agregarPedido(Pedido pedido) {
        if (!"En curso".equals(estado)) {
            throw new IllegalStateException("No se pueden agregar pedidos a un servicio finalizado.");
        }
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
    }
    
    // Eliminar pedido del servicio 
    
    public void eliminarPedido(Pedido pedido){
        if (!"En curso".equals(estado)) {
            throw new IllegalStateException("No se pueden eliminar pedidos a un servicio finalizado.");
        }
        pedidos.remove(pedido);
        montoTotal -= pedido.calcularTotal();
    }
    
    

    // Confirma el servicio y valida el stock
    public void confirmar() throws SinStockException {
        validarStockPedidos();
        estado = "Confirmado";
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
    public void finalizar() {
        if (!"Confirmado".equals(estado)) {
            throw new IllegalStateException("El servicio debe estar confirmado para finalizar.");
        }
        aplicarBeneficiosCliente();
        estado = "Finalizado";
    }

    private void aplicarBeneficiosCliente() {
        
        montoTotal = cliente.getTipoCliente()
            .aplicarBeneficio(pedidos, montoTotal);
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
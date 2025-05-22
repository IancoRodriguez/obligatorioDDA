package Servicios;

import Dominio.Excepciones.PedidoException;
import Dominio.Excepciones.SinStockException;
import Dominio.Item;
import Dominio.Pedido;
import Dominio.Servicio;
import java.util.ArrayList;
import java.util.List;

public class ServicioPedidos {

    private List<Pedido> pedidos;

    public ServicioPedidos() {
        this.pedidos = new ArrayList();
    }
    
    
    
    
    public Pedido registrarPedido(Item item,String comentario, Servicio servicio) throws SinStockException {
        validarEstadoServicio(servicio);
        validarStockItem(item);
        Pedido pedido = new Pedido(item,comentario, servicio);
        descontarStockItem(item);
        servicio.agregarPedido(pedido);
        return pedido;
    }

    public void eliminarPedido(Pedido pedido, Servicio servicio)  {
        validarEstadoPedido(pedido);
        reintegrarStockItem(pedido.getItem());
        servicio.eliminarPedido(pedido);
    }

    public void validarPedidosServicio(Servicio servicio) throws SinStockException {
        for (Pedido pedido : servicio.getPedidos()) {
            validarStockItem(pedido.getItem());
        }
    }

    // ======================
    // Métodos auxiliares (sin StockManager)
    // ======================
    
    private void validarEstadoServicio(Servicio servicio) {
        if (!servicio.getEstado().equals("En curso")) {
            throw new IllegalStateException("No se pueden agregar pedidos a un servicio finalizado");
        }
    }

    private void validarStockItem(Item item) throws SinStockException {
        for (Ingrediente ingrediente : item.getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            if (insumo.getStockActual() < ingrediente.getCantidad()) {
                throw new SinStockException("Stock insuficiente para: " + insumo.getNombre());
            }
        }
    }

    private void descontarStockItem(Item item) {
        for (Ingrediente ingrediente : item.getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.descontarStock(ingrediente.getCantidad());
        }
    }

    private void reintegrarStockItem(Item item) {
        for (Ingrediente ingrediente : item.getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.aumentarStock(ingrediente.getCantidad());
        }
    }

    private void validarEstadoPedido(Pedido pedido) {
        if (pedido.getEstado().equals("Confirmado")) {
            throw new OperacionNoPermitidaException("No se puede eliminar un pedido confirmado");
        }
    }
}

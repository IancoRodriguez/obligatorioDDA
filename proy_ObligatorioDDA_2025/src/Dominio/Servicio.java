package Dominio;

import Dominio.Estados.Confirmado;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Observer.Observable;
import java.util.ArrayList;
import java.util.List;

public class Servicio extends Observable{

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
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    // Eliminar pedido del servicio 
    public void eliminarPedido(Pedido pedido) throws ServicioException {
        // 1. Valida si se puede eliminar (solo permitido para estado Confirmado)
        pedido.validarEliminacion();

        // 2. Reintegrar stock (ya que sabemos que está confirmado)
        reintegrarStock(pedido);

        // 3. Eliminar de la lista de pedidos
        pedidos.remove(pedido);

        // 4. Actualizar monto total
        montoTotal -= pedido.calcularTotal();
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    // Confirma el servicio y valida el stock
    public void confirmar() throws StockException {
        try {
            validarStockPedidos();
            for (Pedido p : pedidos) {
                p.confirmar();
                for (Ingrediente i : p.getItem().getIngredientes()) {
                    i.getInsumo().consumirStock(i.getCantidad());
                }
            }

        } catch (StockException e) {
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
      

    }

    public void aplicarBeneficiosCliente() {

        montoTotal = cliente.getTipoCliente()
                .aplicarBeneficio(pedidos, montoTotal);
    }

    private void reintegrarStock(Pedido pedido) {
        Item item = pedido.getItem();  // Obtener el único ítem del pedido

        for (Ingrediente ingrediente : item.getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            double cantidad = ingrediente.getCantidad();  // Cantidad por unidad

            // Convertir a entero (redondeando) si es necesario
            int cantidadEntera = (int) Math.round(cantidad);

            // Usar el método existente de Insumo
            insumo.agregarStock(cantidadEntera);
        }

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

    public List<Item> getItemsPorUP(UnidadProcesadora UP) {

        List<Item> itemsDeLaUP = new ArrayList();
        for (Pedido p : pedidos) {
            if (p.getItem().getUnidadProcesadora().equals(UP)) {
                itemsDeLaUP.add(p.getItem());
            }
        }

        return itemsDeLaUP;
    }

    public List<String> mostrarPedidosPorUP(UnidadProcesadora UP) {

        List<String> res = new ArrayList();

        for (Pedido pedido : pedidos) {
            StringBuilder str = new StringBuilder();
            if (pedido.getItem().getUnidadProcesadora().equals(UP)) {
                str.append(pedido.getItem().getNombre() + " - ");
                str.append("Cliente: " + this.cliente.getNombreCompleto() + " - ");
                str.append(pedido.getFechaHora());

                res.add(str.toString());
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

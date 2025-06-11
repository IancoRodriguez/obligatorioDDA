package Dominio;

import Dominio.Estados.Confirmado;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Observer.Observable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servicio extends Observable {

    private Cliente cliente;
    private List<Pedido> pedidos;
    private double montoTotal;
    
    // NUEVO: Para trackear qué pedidos ya consumieron stock
    private List<Pedido> pedidosConfirmados;

    public Servicio(Cliente cliente) {
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
        this.pedidosConfirmados = new ArrayList<>();
    }

    // Agrega un pedido al servicio
    public void agregarPedido(Pedido pedido) throws ServicioException {
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    // Eliminar pedido del servicio 
    public void eliminarPedido(Pedido pedido) throws ServicioException {
        // Si el pedido estaba confirmado, reintegrar stock
        if (pedidosConfirmados.contains(pedido)) {
            reintegrarStock(pedido);
            pedidosConfirmados.remove(pedido);
        }

        // Eliminar de la lista de pedidos
        pedidos.remove(pedido);

        // Actualizar monto total
        montoTotal -= pedido.calcularTotal();
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    public void confirmar() throws StockException {
        // Solo confirmar pedidos que no estén ya confirmados
        List<Pedido> pedidosPorConfirmar = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                pedidosPorConfirmar.add(pedido);
            }
        }
        
        if (pedidosPorConfirmar.isEmpty()) {
            return; // No hay nada que confirmar
        }

        // Calcular requerimientos solo de pedidos nuevos
        Map<Insumo, Integer> requerimientos = calcularRequerimientos(pedidosPorConfirmar);

        // Validar stock disponible
        validarStockDisponible(requerimientos);

        // Consumir stock
        consumirStock(requerimientos);

        // Marcar pedidos como confirmados
        for (Pedido pedido : pedidosPorConfirmar) {
            pedido.getEstado().confirmar(pedido);
            pedidosConfirmados.add(pedido);
        }
    }

    private Map<Insumo, Integer> calcularRequerimientos(List<Pedido> pedidosAConfirmar) {
        Map<Insumo, Integer> requerimientos = new HashMap<>();
        
        for (Pedido pedido : pedidosAConfirmar) {
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                int cantidad = ingrediente.getCantidad();
                requerimientos.merge(insumo, cantidad, Integer::sum);
            }
        }
        
        return requerimientos;
    }

    private void validarStockDisponible(Map<Insumo, Integer> requerimientos) throws StockException {
        for (Map.Entry<Insumo, Integer> entry : requerimientos.entrySet()) {
            Insumo insumo = entry.getKey();
            int cantidadRequerida = entry.getValue();
            
            if (insumo.getStock() < cantidadRequerida) {
                throw new StockException(
                    "Stock insuficiente de " + insumo.getNombre() + 
                    " (necesario: " + cantidadRequerida + 
                    ", disponible: " + insumo.getStock() + ")"
                );
            }
        }
    }

    private void consumirStock(Map<Insumo, Integer> requerimientos) throws StockException {
        for (Map.Entry<Insumo, Integer> entry : requerimientos.entrySet()) {
            Insumo insumo = entry.getKey();
            int cantidad = entry.getValue();
            insumo.consumirStock(cantidad);
        }
    }

    private void reintegrarStock(Pedido pedido) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            int cantidad = ingrediente.getCantidad();
            insumo.agregarStock(cantidad);
        }
    }

    public List<Pedido> pedidosConStock() {
        List<Pedido> aux = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getItem().tieneStockDisponible()) {
                aux.add(pedido);
            }
        }
        return aux;
    }

    public List<Pedido> pedidosEliminados() {
        List<Pedido> aux = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.getItem().tieneStockDisponible()) {
                aux.add(pedido);
            }
        }
        return aux;
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

    // ======================
    // Getters y Setters
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
        List<Item> itemsDeLaUP = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getItem().getUnidadProcesadora().equals(UP)) {
                itemsDeLaUP.add(p.getItem());
            }
        }
        return itemsDeLaUP;
    }
    
    public List<Pedido> mostrarPedidosPorUP(UnidadProcesadora UP) {
        List<Pedido> res = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if(pedido.getItem().getUnidadProcesadora().equals(UP)){
                res.add(pedido);
            }
        }
        return res;
    }
    
    public String getNombreCliente(){
        return this.cliente.getNombreCompleto();
    }
    
    // NUEVO: Método para debugging
    public boolean estaPedidoConfirmado(Pedido pedido) {
        return pedidosConfirmados.contains(pedido);
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
// Confirma el servicio y valida el stock
//    public void confirmar() throws StockException {
//        try {
//            validarStockPedidos();
//            for (Pedido p : pedidos) {
//                if(p.getEstado().esSinConfirmar()){
//                    for (Ingrediente i : p.getItem().getIngredientes()) {
//                        i.getInsumo().consumirStock(i.getCantidad());
//                    }
//                }
//                p.confirmar();
//            }
//
//        } catch (StockException e) {
//           throw e;
//        }
//
//    } 
// versio con map 
//
//public void confirmar() throws StockException {
//        // 1) Validación global con bucles anidados
//        for (Pedido p1 : pedidos) {
//            // Para cada ingrediente que *podría* consumirse en p1...
//            for (Ingrediente ing1 : p1.getEstado().ingredientesParaConfirmar(p1)) {
//                Insumo ins = ing1.getInsumo();
//                int totalRequerido = 0;
//
//                // Sumo la demanda de ese mismo insumo en todos los pedidos
//                for (Pedido p2 : pedidos) {
//                    for (Ingrediente ing2 : p2.getEstado().ingredientesParaConfirmar(p2)) {
//                        if (ing2.getInsumo().equals(ins)) {
//                            totalRequerido += ing2.getCantidad();
//                        }
//                    }
//                }
//
//                // Compruebo stock
//                if (ins.getStock() < totalRequerido) {
//                    throw new StockException(
//                        "Stock insuficiente de " 
//                        + ins.getNombre() 
//                        + " (necesario " + totalRequerido 
//                        + ", disponible " + ins.getStock() + ")"
//                    );
//                }
//            }
//        }
//
//        // 2) Si todo pasó, confirmo uno a uno
//        for (Pedido p : pedidos) {
//            p.confirmar();  // cada EstadoPedido.confirmar() hará validación y consumo
//        }
//    }

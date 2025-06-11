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

    // Para trackear qué pedidos ya consumieron stock
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
        // Obtener pedidos pendientes de confirmación
        List<Pedido> pedidosPorConfirmar = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                pedidosPorConfirmar.add(pedido);
            }
        }

        if (pedidosPorConfirmar.isEmpty()) {
            return; // No hay nada que confirmar
        }

        // Calcular requerimientos totales
        Map<Insumo, Integer> requerimientosTotales = calcularRequerimientos(pedidosPorConfirmar);

        // Separar pedidos viables de los que no tienen stock
        List<Pedido> pedidosViables = new ArrayList<>();
        List<Pedido> pedidosSinStock = new ArrayList<>();

        determinarPedidosViables(pedidosPorConfirmar, requerimientosTotales, pedidosViables, pedidosSinStock);

        // Procesar eliminaciones automáticas PRIMERO
        if (!pedidosSinStock.isEmpty()) {
            procesarEliminacionesAutomaticas(pedidosSinStock);
        }

        // Confirmar pedidos viables si los hay
        if (!pedidosViables.isEmpty()) {
            confirmarPedidosViables(pedidosViables);
        }
    }

    private void determinarPedidosViables(List<Pedido> pedidosPorConfirmar,
            Map<Insumo, Integer> requerimientosTotales,
            List<Pedido> pedidosViables,
            List<Pedido> pedidosSinStock) {

        // Crear copia del stock disponible para simular consumo
        Map<Insumo, Integer> stockSimulado = new HashMap<>();
        for (Insumo insumo : requerimientosTotales.keySet()) {
            stockSimulado.put(insumo, insumo.getStock());
        }

        // Procesar pedidos en orden (FIFO - primero en entrar, primero en confirmarse)
        for (Pedido pedido : pedidosPorConfirmar) {
            boolean pedidoViable = true;

            // Verificar si este pedido específico se puede satisfacer
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                int cantidadNecesaria = ingrediente.getCantidad();

                Integer stockDisponible = stockSimulado.get(insumo);
                if (stockDisponible == null || stockDisponible < cantidadNecesaria) {
                    pedidoViable = false;
                    break;
                }
            }

            if (pedidoViable) {
                pedidosViables.add(pedido);
                // Simular consumo de stock para el próximo pedido
                for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                    Insumo insumo = ingrediente.getInsumo();
                    int cantidadNecesaria = ingrediente.getCantidad();
                    stockSimulado.put(insumo, stockSimulado.get(insumo) - cantidadNecesaria);
                }
            } else {
                pedidosSinStock.add(pedido);
            }
        }
    }

    private void procesarEliminacionesAutomaticas(List<Pedido> pedidosSinStock) {
        List<String> mensajesEliminacion = new ArrayList<>();

        for (Pedido pedido : pedidosSinStock) {
            // Crear mensaje de eliminación
            String mensaje = "Nos hemos quedado sin stock de "
                    + pedido.getItem().getNombre()
                    + " y no pudimos avisarte antes!";
            mensajesEliminacion.add(mensaje);

            // Eliminar pedido del servicio
            pedidos.remove(pedido);
            montoTotal -= pedido.calcularTotal();
        }

        // CORREGIDO: Notificar con los mensajes directamente como objeto
        if (!mensajesEliminacion.isEmpty()) {
            // Enviar los mensajes como el objeto del evento (no como segundo parámetro)
            notificar(mensajesEliminacion);
            // Luego actualizar el monto
            notificar(Evento.MONTO_ACTUALIZADO);
        }
    }

    private void confirmarPedidosViables(List<Pedido> pedidosViables) throws StockException {
        // Calcular requerimientos de pedidos viables
        Map<Insumo, Integer> requerimientosViables = calcularRequerimientos(pedidosViables);

        // Consumir stock real
        consumirStock(requerimientosViables);

        // Confirmar pedidos
        for (Pedido pedido : pedidosViables) {
            pedido.getEstado().confirmar(pedido);
            pedidosConfirmados.add(pedido);
        }
    }

    private void separarPedidosPorStock(List<Pedido> pedidosPorConfirmar,
            List<Pedido> pedidosViables,
            List<Pedido> pedidosSinStock) {

        for (Pedido pedido : pedidosPorConfirmar) {
            if (pedidoTieneStockDisponible(pedido)) {
                pedidosViables.add(pedido);
            } else {
                pedidosSinStock.add(pedido);
            }
        }
    }

    private boolean pedidoTieneStockDisponible(Pedido pedido) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            if (ingrediente.getInsumo().getStock() < ingrediente.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    private List<Pedido> identificarYEliminarPedidosSinStock(List<Pedido> pedidosAEvaluar) {
        List<Pedido> pedidosAEliminar = new ArrayList<>();

        for (Pedido pedido : pedidosAEvaluar) {
            if (!pedido.getItem().tieneStockDisponible()) {
                pedidosAEliminar.add(pedido);
            }
        }

        // Eliminar los pedidos sin stock
        for (Pedido pedido : pedidosAEliminar) {
            try {
                eliminarPedido(pedido);
            } catch (ServicioException e) {
                // Log del error, pero continuar
                System.err.println("Error al eliminar pedido sin stock: " + e.getMessage());
            }
        }

        return pedidosAEliminar;
    }

    private void confirmarPedidosConStock(List<Pedido> pedidosAConfirmar) throws StockException {
        // Calcular requerimientos solo de pedidos nuevos
        Map<Insumo, Integer> requerimientos = calcularRequerimientos(pedidosAConfirmar);

        // Validar stock disponible
        validarStockDisponible(requerimientos);

        // Consumir stock
        consumirStock(requerimientos);

        // Marcar pedidos como confirmados
        for (Pedido pedido : pedidosAConfirmar) {
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
                        "Stock insuficiente de " + insumo.getNombre()
                        + " (necesario: " + cantidadRequerida
                        + ", disponible: " + insumo.getStock() + ")"
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

    // Clase interna para el resultado de confirmación
    public static class ConfirmacionResult {

        private final List<Pedido> pedidosEliminados;
        private final boolean confirmacionExitosa;

        public ConfirmacionResult(List<Pedido> pedidosEliminados, boolean confirmacionExitosa) {
            this.pedidosEliminados = pedidosEliminados;
            this.confirmacionExitosa = confirmacionExitosa;
        }

        public List<Pedido> getPedidosEliminados() {
            return pedidosEliminados;
        }

        public boolean isConfirmacionExitosa() {
            return confirmacionExitosa;
        }

        public boolean hayPedidosEliminados() {
            return !pedidosEliminados.isEmpty();
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
            if (pedido.getItem().getUnidadProcesadora().equals(UP)) {
                res.add(pedido);
            }
        }
        return res;
    }

    public String getNombreCliente() {
        return this.cliente.getNombreCompleto();
    }

    // Método para debugging
    public boolean estaPedidoConfirmado(Pedido pedido) {
        return pedidosConfirmados.contains(pedido);
    }
}

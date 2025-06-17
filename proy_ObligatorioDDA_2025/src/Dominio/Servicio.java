package Dominio;

import Dominio.Estados.Confirmado;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Observer.Observable;
import Dominio.Observer.Observador;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servicio extends Observable implements Observador {

    private Cliente cliente;
    private List<Pedido> pedidos;
    private double montoTotal;
    private boolean confirmandoPedidos = false;

    // Para ver que pedidos ya consumieron stock
    private List<Pedido> pedidosConfirmados;

    public Servicio(Cliente cliente) {
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
        this.pedidosConfirmados = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) throws ServicioException {
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();

        // Suscribirse a los insumos del nuevo pedido
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.desuscribir(this); // Evitar duplicados
            insumo.subscribir(this);
        }

        notificar(Evento.MONTO_ACTUALIZADO);
    }

    public void eliminarPedido(Pedido pedido) throws ServicioException {
        
        pedido.validarEliminacion();
        // Si el pedido estaba confirmado, reintegrar stock
        if (pedidosConfirmados.contains(pedido)) {
            reintegrarStock(pedido);
            pedidosConfirmados.remove(pedido);
        }

        // Eliminar de la lista de pedidos
        pedidos.remove(pedido);
        notificar(Evento.PEDIDO_CAMBIO_ESTADO);

        // Actualizar monto total
        montoTotal -= pedido.calcularTotal();
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    public ConfirmacionResult confirmar() throws StockException, ServicioException {
        // Obtener pedidos pendientes de confirmación
        List<Pedido> pedidosPorConfirmar = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                pedidosPorConfirmar.add(pedido);
            }
        }

        if (pedidosPorConfirmar.isEmpty()) {
            return new ConfirmacionResult(new ArrayList<>(), new ArrayList<>());
        }

        // Separar pedidos viables de los no viables
        List<Pedido> pedidosViables = new ArrayList<>();
        List<Pedido> pedidosSinStock = new ArrayList<>();

        determinarPedidosViables(pedidosPorConfirmar, pedidosViables, pedidosSinStock);

        // Procesar eliminaciones automáticas
        List<Pedido> pedidosEliminados = new ArrayList<>();
        if (!pedidosSinStock.isEmpty()) {
            pedidosEliminados.addAll(pedidosSinStock);
            procesarEliminacionesAutomaticas(pedidosSinStock);
        }

        // Confirmar pedidos viables
        List<Pedido> pedidosConfirmadosEnEstaOperacion = new ArrayList<>();
        if (!pedidosViables.isEmpty()) {
            pedidosConfirmadosEnEstaOperacion.addAll(pedidosViables);
            confirmarPedidosViables(pedidosViables);
        }

        // Retornar resultado
        return new ConfirmacionResult(pedidosEliminados, pedidosConfirmadosEnEstaOperacion);
    }

    
    public static class ConfirmacionResult {

        private final List<Pedido> pedidosEliminados;
        private final List<Pedido> pedidosConfirmados;

        public ConfirmacionResult(List<Pedido> pedidosEliminados, List<Pedido> pedidosConfirmados) {
            this.pedidosEliminados = new ArrayList<>(pedidosEliminados);
            this.pedidosConfirmados = new ArrayList<>(pedidosConfirmados);
        }

        public List<Pedido> getPedidosEliminados() {
            return pedidosEliminados;
        }

        public List<Pedido> getPedidosConfirmados() {
            return pedidosConfirmados;
        }

        public boolean hayPedidosEliminados() {
            return !pedidosEliminados.isEmpty();
        }

        public boolean hayPedidosConfirmados() {
            return !pedidosConfirmados.isEmpty();
        }

        public boolean isConfirmacionExitosa() {
            return hayPedidosConfirmados();
        }
    }

    private void determinarPedidosViables(List<Pedido> pedidosPorConfirmar,
            List<Pedido> pedidosViables,
            List<Pedido> pedidosSinStock) {

        Map<Insumo, Integer> stockDisponible = new HashMap<>();

        // Inicializar con el stock actual de cada insumo
        for (Pedido pedido : pedidosPorConfirmar) {
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                if (!stockDisponible.containsKey(insumo)) {
                    stockDisponible.put(insumo, insumo.getStock());
                }
            }
        }

        // Procesar pedidos en orden 
        for (Pedido pedido : pedidosPorConfirmar) {
            boolean pedidoViable = true;

            // Verificar si este pedido específico se puede satisfacer
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                int cantidadNecesaria = ingrediente.getCantidad();

                Integer stockActual = stockDisponible.get(insumo);
                if (stockActual == null || stockActual < cantidadNecesaria) {
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
                    stockDisponible.put(insumo, stockDisponible.get(insumo) - cantidadNecesaria);
                }
            } else {
                pedidosSinStock.add(pedido);
            }
        }
    }

    private void confirmarPedidosViables(List<Pedido> pedidosViables) throws StockException, ServicioException {
        // Bandera antes de consumir stock
        confirmandoPedidos = true;

        try {
            // Calcular requerimientos de pedidos viables
            Map<Insumo, Integer> requerimientosViables = calcularRequerimientos(pedidosViables);

            // Consumir stock real - esto dispara las notificaciones a otros servicios
            consumirStock(requerimientosViables);

            // Confirmar pedidos
            for (Pedido pedido : pedidosViables) {
                pedido.getEstado().confirmar();
                pedidosConfirmados.add(pedido);
            }

            // Notificar que hay pedidos confirmados (para actualizar la tabla)
            notificar("PEDIDOS_CONFIRMADOS");
            notificar(Evento.PEDIDO_CAMBIO_ESTADO);
        } catch (ServicioException ex) {
            throw ex;
        } finally {
            confirmandoPedidos = false;
        }
    }

    private void procesarEliminacionesAutomaticas(List<Pedido> pedidosSinStock) {
        List<String> mensajesEliminacion = new ArrayList<>();

        for (Pedido pedido : pedidosSinStock) {
            String mensaje = "Nos hemos quedado sin stock de "
                    + pedido.getItem().getNombre()
                    + " y no pudimos avisarte antes!";
            mensajesEliminacion.add(mensaje);

            // Eliminar pedido del servicio
            pedidos.remove(pedido);
            montoTotal -= pedido.calcularTotal();
        }

        if (!mensajesEliminacion.isEmpty()) {
            // Enviar los mensajes como el objeto del evento
            notificar(mensajesEliminacion);
            // Luego actualizar el monto
            notificar(Evento.MONTO_ACTUALIZADO);
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

    private void confirmarPedidosConStock(List<Pedido> pedidosAConfirmar) throws StockException, ServicioException {
        // Calcular requerimientos solo de pedidos nuevos
        Map<Insumo, Integer> requerimientos = calcularRequerimientos(pedidosAConfirmar);

        // Validar stock disponible
        validarStockDisponible(requerimientos);
        consumirStock(requerimientos);

        // CAmbiar estado de pedidos a confirmados
        for (Pedido pedido : pedidosAConfirmar) {
            pedido.getEstado().confirmar();
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
                                + ", disponible: " + insumo.getStock() + ")");
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

    @Override
    public void notificar(Observable origen, Object evento) {
        // Si es notificación de stock actualizado de un insumo
        if (origen instanceof Insumo && evento == Observable.Evento.STOCK_ACTUALIZADO) {
            // Verificar si no hay un servicio en proceso de confirmación
            if (!confirmandoPedidos && !hayAlgunServicioConfirmando()) {
                verificarYEliminarPedidosSinStock();
            }
        }
    }

    
    private boolean hayAlgunServicioConfirmando() {
        return ServicioConfirmando.hayConfirmacionEnProceso();
    }

    
    public static class ServicioConfirmando {
        private static boolean confirmacionEnProceso = false;

        public static void iniciarConfirmacion() {
            confirmacionEnProceso = true;
        }

        public static void finalizarConfirmacion() {
            confirmacionEnProceso = false;
        }

        public static boolean hayConfirmacionEnProceso() {
            return confirmacionEnProceso;
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

    // Método para verificar si un pedido ya está confirmado
    public boolean estaPedidoConfirmado(Pedido pedido) {
        return pedidosConfirmados.contains(pedido);
    }

    // Método para obtener solo los pedidos pendientes de confirmación
    public List<Pedido> getPedidosPendientes() {
        List<Pedido> pendientes = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                pendientes.add(pedido);
            }
        }
        return pendientes;
    }

    
    public void suscribirseAInsumos() {
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) { 
                for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                    Insumo insumo = ingrediente.getInsumo();
                    insumo.desuscribir(this); 
                    insumo.subscribir(this);
                }
            }
        }
    }

    
    public void desuscribirseDeInsumos() {
        for (Pedido pedido : pedidos) {
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                insumo.desuscribir(this);
            }
        }
    }

    
    private void verificarYEliminarPedidosSinStock() {
        List<Pedido> pedidosAEliminar = new ArrayList<>();

        // SOLO verificar pedidos PENDIENTES (no confirmados)
        for (Pedido pedido : new ArrayList<>(pedidos)) { 
            if (!pedidosConfirmados.contains(pedido)) { // Solo pedidos pendientes
                // Verificar si todos los ingredientes del pedido tienen stock suficiente
                boolean tieneStockCompleto = true;
                for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                    Insumo insumo = ingrediente.getInsumo();
                    if (insumo.getStock() < ingrediente.getCantidad()) {
                        tieneStockCompleto = false;
                        break;
                    }
                }

                if (!tieneStockCompleto) {
                    pedidosAEliminar.add(pedido);
                }
            }
        }

        // Eliminar pedidos sin stock y generar mensajes
        if (!pedidosAEliminar.isEmpty()) {
            List<String> mensajesEliminacion = new ArrayList<>();

            for (Pedido pedido : pedidosAEliminar) {
                String mensaje = "Nos hemos quedado sin stock de "
                        + pedido.getItem().getNombre()
                        + " y no pudimos avisarte antes!";
                mensajesEliminacion.add(mensaje);

                // Eliminar pedido del servicio
                pedidos.remove(pedido);
                montoTotal -= pedido.calcularTotal();
            }

            notificar(mensajesEliminacion);
            notificar(Evento.MONTO_ACTUALIZADO);
        }
    }
}

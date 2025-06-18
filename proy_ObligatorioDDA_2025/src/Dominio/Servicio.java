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
    private List<Pedido> pedidosConfirmados;

    public Servicio(Cliente cliente) {
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
        this.pedidosConfirmados = new ArrayList<>();
    }

    public void agregarPedido(Pedido pedido) throws ServicioException {
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
        suscribirseAInsumosDePedido(pedido);
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    public void eliminarPedido(Pedido pedido) throws ServicioException {
        pedido.validarEliminacion();
        
        if (pedidosConfirmados.contains(pedido)) {
            reintegrarStock(pedido);
            pedidosConfirmados.remove(pedido);
        }

        pedidos.remove(pedido);
        montoTotal -= pedido.calcularTotal();
        
        notificar(Evento.PEDIDO_CAMBIO_ESTADO);
        notificar(Evento.MONTO_ACTUALIZADO);
    }

    public ConfirmacionResult confirmar() throws StockException, ServicioException {
        List<Pedido> pedidosPendientes = obtenerPedidosPendientes();
        if (pedidosPendientes.isEmpty()) {
            return new ConfirmacionResult(new ArrayList<>(), new ArrayList<>());
        }

        ClasificacionPedidos clasificacion = clasificarPedidosPorStock(pedidosPendientes);
        List<Pedido> pedidosEliminados = procesarPedidosSinStock(clasificacion.pedidosSinStock);
        List<Pedido> pedidosConfirmados = procesarPedidosViables(clasificacion.pedidosViables);

        return new ConfirmacionResult(pedidosEliminados, pedidosConfirmados);
    }

    @Override
    public void notificar(Observable origen, Object evento) {
        if (debeVerificarPedidosPorFaltaDeStock(origen, evento)) {
            verificarYEliminarPedidosSinStock();
        }
    }

    public void finalizar() throws ServicioException {
        for (Pedido p : pedidos) {
            p.finalizar();
        }
    }

    public void aplicarBeneficiosCliente() {
        montoTotal = cliente.getTipoCliente().aplicarBeneficio(pedidos, montoTotal);
    }

    // ========== MÉTODOS AUXILIARES DE CONFIRMACIÓN ==========
    
    private List<Pedido> obtenerPedidosPendientes() {
        List<Pedido> pendientes = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                pendientes.add(pedido);
            }
        }
        return pendientes;
    }

    private ClasificacionPedidos clasificarPedidosPorStock(List<Pedido> pedidosPendientes) {
        List<Pedido> pedidosViables = new ArrayList<>();
        List<Pedido> pedidosSinStock = new ArrayList<>();
        
        Map<Insumo, Integer> stockSimulado = inicializarStockSimulado(pedidosPendientes);
        
        for (Pedido pedido : pedidosPendientes) {
            if (pedidoPuedeSatisfacerse(pedido, stockSimulado)) {
                pedidosViables.add(pedido);
                simularConsumoDeStock(pedido, stockSimulado);
            } else {
                pedidosSinStock.add(pedido);
            }
        }
        
        return new ClasificacionPedidos(pedidosViables, pedidosSinStock);
    }

    private Map<Insumo, Integer> inicializarStockSimulado(List<Pedido> pedidos) {
        Map<Insumo, Integer> stockDisponible = new HashMap<>();
        for (Pedido pedido : pedidos) {
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                if (!stockDisponible.containsKey(insumo)) {
                    stockDisponible.put(insumo, insumo.getStock());
                }
            }
        }
        return stockDisponible;
    }

    private boolean pedidoPuedeSatisfacerse(Pedido pedido, Map<Insumo, Integer> stockDisponible) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            int cantidadNecesaria = ingrediente.getCantidad();
            Integer stockActual = stockDisponible.get(insumo);
            
            if (stockActual == null || stockActual < cantidadNecesaria) {
                return false;
            }
        }
        return true;
    }

    private void simularConsumoDeStock(Pedido pedido, Map<Insumo, Integer> stockDisponible) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            int cantidadNecesaria = ingrediente.getCantidad();
            stockDisponible.put(insumo, stockDisponible.get(insumo) - cantidadNecesaria);
        }
    }

    private List<Pedido> procesarPedidosSinStock(List<Pedido> pedidosSinStock) {
        if (pedidosSinStock.isEmpty()) {
            return new ArrayList<>();
        }
        
        eliminarPedidosAutomaticamente(pedidosSinStock);
        return new ArrayList<>(pedidosSinStock);
    }

    private List<Pedido> procesarPedidosViables(List<Pedido> pedidosViables) 
            throws StockException, ServicioException {
        if (pedidosViables.isEmpty()) {
            return new ArrayList<>();
        }
        
        confirmarPedidosViables(pedidosViables);
        return new ArrayList<>(pedidosViables);
    }

    private void confirmarPedidosViables(List<Pedido> pedidosViables) 
            throws StockException, ServicioException {
        confirmandoPedidos = true;
        
        try {
            Map<Insumo, Integer> requerimientos = calcularRequerimientos(pedidosViables);
            consumirStock(requerimientos);
            
            for (Pedido pedido : pedidosViables) {
                pedido.getEstado().confirmar();
                pedidosConfirmados.add(pedido);
            }
            
            notificar("PEDIDOS_CONFIRMADOS");
            notificar(Evento.PEDIDO_CAMBIO_ESTADO);
        } finally {
            confirmandoPedidos = false;
        }
    }

    private void eliminarPedidosAutomaticamente(List<Pedido> pedidosSinStock) {
        List<String> mensajesEliminacion = crearMensajesEliminacion(pedidosSinStock);
        eliminarPedidosDeLaLista(pedidosSinStock);
        
        if (!mensajesEliminacion.isEmpty()) {
            notificar(mensajesEliminacion);
            notificar(Evento.MONTO_ACTUALIZADO);
        }
    }

    private List<String> crearMensajesEliminacion(List<Pedido> pedidosSinStock) {
        List<String> mensajes = new ArrayList<>();
        for (Pedido pedido : pedidosSinStock) {
            String mensaje = "Nos hemos quedado sin stock de " + 
                           pedido.getItem().getNombre() + 
                           " y no pudimos avisarte antes!";
            mensajes.add(mensaje);
        }
        return mensajes;
    }

    private void eliminarPedidosDeLaLista(List<Pedido> pedidosAEliminar) {
        for (Pedido pedido : pedidosAEliminar) {
            pedidos.remove(pedido);
            montoTotal -= pedido.calcularTotal();
        }
    }

    // ========== MÉTODOS AUXILIARES DE STOCK ==========
    
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

    private void consumirStock(Map<Insumo, Integer> requerimientos) throws StockException {
        validarStockDisponible(requerimientos);
        ejecutarConsumoDeStock(requerimientos);
    }

    private void validarStockDisponible(Map<Insumo, Integer> requerimientos) throws StockException {
        for (Map.Entry<Insumo, Integer> entry : requerimientos.entrySet()) {
            Insumo insumo = entry.getKey();
            int cantidadRequerida = entry.getValue();
            
            if (insumo.getStock() < cantidadRequerida) {
                throw new StockException(
                    "Stock insuficiente de " + insumo.getNombre() +
                    " (necesario: " + cantidadRequerida +
                    ", disponible: " + insumo.getStock() + ")");
            }
        }
    }

    private void ejecutarConsumoDeStock(Map<Insumo, Integer> requerimientos) throws StockException {
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

    // ========== MÉTODOS AUXILIARES DE NOTIFICACIONES ==========
    
    private boolean debeVerificarPedidosPorFaltaDeStock(Observable origen, Object evento) {
        return origen instanceof Insumo && 
               evento == Observable.Evento.STOCK_ACTUALIZADO && 
               !confirmandoPedidos && 
               !ServicioConfirmando.hayConfirmacionEnProceso();
    }

    private void verificarYEliminarPedidosSinStock() {
        List<Pedido> pedidosAEliminar = identificarPedidosSinStock();
        
        if (!pedidosAEliminar.isEmpty()) {
            eliminarPedidosAutomaticamente(pedidosAEliminar);
        }
    }

    private List<Pedido> identificarPedidosSinStock() {
        List<Pedido> pedidosAEliminar = new ArrayList<>();
        
        for (Pedido pedido : new ArrayList<>(pedidos)) {
            if (esPedidoPendiente(pedido) && !pedidoTieneStockCompleto(pedido)) {
                pedidosAEliminar.add(pedido);
            }
        }
        
        return pedidosAEliminar;
    }

    private boolean esPedidoPendiente(Pedido pedido) {
        return !pedidosConfirmados.contains(pedido);
    }

    private boolean pedidoTieneStockCompleto(Pedido pedido) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            if (insumo.getStock() < ingrediente.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    private void suscribirseAInsumosDePedido(Pedido pedido) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.desuscribir(this);
            insumo.subscribir(this);
        }
    }

    // ========== CLASES AUXILIARES ==========
    
    public static class ConfirmacionResult {
        private final List<Pedido> pedidosEliminados;
        private final List<Pedido> pedidosConfirmados;

        public ConfirmacionResult(List<Pedido> pedidosEliminados, List<Pedido> pedidosConfirmados) {
            this.pedidosEliminados = new ArrayList<>(pedidosEliminados);
            this.pedidosConfirmados = new ArrayList<>(pedidosConfirmados);
        }

        public List<Pedido> getPedidosEliminados() { return pedidosEliminados; }
        public List<Pedido> getPedidosConfirmados() { return pedidosConfirmados; }
        public boolean hayPedidosEliminados() { return !pedidosEliminados.isEmpty(); }
        public boolean hayPedidosConfirmados() { return !pedidosConfirmados.isEmpty(); }
        public boolean isConfirmacionExitosa() { return hayPedidosConfirmados(); }
    }

    private static class ClasificacionPedidos {
        final List<Pedido> pedidosViables;
        final List<Pedido> pedidosSinStock;

        ClasificacionPedidos(List<Pedido> pedidosViables, List<Pedido> pedidosSinStock) {
            this.pedidosViables = pedidosViables;
            this.pedidosSinStock = pedidosSinStock;
        }
    }

    public static class ServicioConfirmando {
        private static boolean confirmacionEnProceso = false;

        public static void iniciarConfirmacion() { confirmacionEnProceso = true; }
        public static void finalizarConfirmacion() { confirmacionEnProceso = false; }
        public static boolean hayConfirmacionEnProceso() { return confirmacionEnProceso; }
    }

    // ========== GETTERS Y MÉTODOS DE CONSULTA ==========
    
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<Pedido> getPedidos() { return pedidos; }
    public void setPedidos(List<Pedido> pedidos) { this.pedidos = pedidos; }
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    public String getNombreCliente() { return this.cliente.getNombreCompleto(); }
    public boolean estaPedidoConfirmado(Pedido pedido) { return pedidosConfirmados.contains(pedido); }
    public List<Pedido> getPedidosPendientes() { return obtenerPedidosPendientes(); }

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

    public void suscribirseAInsumos() {
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                suscribirseAInsumosDePedido(pedido);
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
}
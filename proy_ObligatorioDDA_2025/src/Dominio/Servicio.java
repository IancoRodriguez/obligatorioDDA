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

        // NUEVO: Suscribirse a los insumos del nuevo pedido
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.desuscribir(this); // Evitar duplicados
            insumo.subscribir(this);
        }

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

    public ConfirmacionResult confirmar() throws StockException {
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

        // PASO 1: Separar pedidos viables de los no viables
        List<Pedido> pedidosViables = new ArrayList<>();
        List<Pedido> pedidosSinStock = new ArrayList<>();

        determinarPedidosViables(pedidosPorConfirmar, pedidosViables, pedidosSinStock);

        // PASO 2: Procesar eliminaciones automáticas
        List<Pedido> pedidosEliminados = new ArrayList<>();
        if (!pedidosSinStock.isEmpty()) {
            pedidosEliminados.addAll(pedidosSinStock);
            procesarEliminacionesAutomaticas(pedidosSinStock);
        }

        // PASO 3: Confirmar pedidos viables
        List<Pedido> pedidosConfirmadosEnEstaOperacion = new ArrayList<>();
        if (!pedidosViables.isEmpty()) {
            pedidosConfirmadosEnEstaOperacion.addAll(pedidosViables);
            confirmarPedidosViables(pedidosViables);
        }

        // Retornar resultado
        return new ConfirmacionResult(pedidosEliminados, pedidosConfirmadosEnEstaOperacion);
    }

    /**
     * CLASE MODIFICADA: ConfirmacionResult Ahora incluye tanto pedidos
     * eliminados como confirmados
     */
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

        // Crear mapa de stock disponible actual
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

        // Procesar pedidos en orden (FIFO - primero en entrar, primero en confirmarse)
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

    private void confirmarPedidosViables(List<Pedido> pedidosViables) throws StockException {
        // NUEVO: Activar bandera antes de consumir stock
        confirmandoPedidos = true;

        try {
            // Calcular requerimientos de pedidos viables
            Map<Insumo, Integer> requerimientosViables = calcularRequerimientos(pedidosViables);

            // Consumir stock real - ESTO dispara las notificaciones a otros servicios
            consumirStock(requerimientosViables);

            // Confirmar pedidos (cambiar estado)
            for (Pedido pedido : pedidosViables) {
                pedido.getEstado().confirmar(pedido);
                pedidosConfirmados.add(pedido);
            }

            // Notificar que hay pedidos confirmados (para actualizar la tabla)
            notificar("PEDIDOS_CONFIRMADOS");

        } finally {
            // NUEVO: Desactivar bandera al finalizar (en bloque finally para garantizar ejecución)
            confirmandoPedidos = false;
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

//    private void confirmarPedidosViables(List<Pedido> pedidosViables) throws StockException {
//        // Calcular requerimientos de pedidos viables
//        Map<Insumo, Integer> requerimientosViables = calcularRequerimientos(pedidosViables);
//
//        // Consumir stock real
//        consumirStock(requerimientosViables);
//
//        // Confirmar pedidos
//        for (Pedido pedido : pedidosViables) {
//            pedido.getEstado().confirmar(pedido);
//            pedidosConfirmados.add(pedido);
//        }
//
//        // CRÍTICO: Notificar que los pedidos cambiaron de estado
//        // Esto fuerza la actualización de la tabla para mostrar los estados actualizados
//        notificar(Evento.PEDIDOS_CONFIRMADOS); // Si existe este evento
//        // O alternativamente:
//        notificar("PEDIDOS_CONFIRMADOS"); // Como string genérico
//    }
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

    @Override
    public void notificar(Observable origen, Object evento) {
        // Si es notificación de stock actualizado de un insumo
        if (origen instanceof Insumo && evento == Observable.Evento.STOCK_ACTUALIZADO) {
            // NUEVO: Solo verificar eliminaciones si NO estamos confirmando nuestros propios pedidos
            if (!confirmandoPedidos) {
                verificarYEliminarPedidosSinStock();
            }
        }
    }

    // Clase interna para el resultado de confirmación
//    public static class ConfirmacionResult {
//
//        private final List<Pedido> pedidosEliminados;
//        private final boolean confirmacionExitosa;
//
//        public ConfirmacionResult(List<Pedido> pedidosEliminados, boolean confirmacionExitosa) {
//            this.pedidosEliminados = pedidosEliminados;
//            this.confirmacionExitosa = confirmacionExitosa;
//        }
//
//        public List<Pedido> getPedidosEliminados() {
//            return pedidosEliminados;
//        }
//
//        public boolean isConfirmacionExitosa() {
//            return confirmacionExitosa;
//        }
//
//        public boolean hayPedidosEliminados() {
//            return !pedidosEliminados.isEmpty();
//        }
//    }

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

    /**
     * Suscribe el servicio a todos los insumos de sus pedidos pendientes para
     * recibir notificaciones de cambios de stock
     */
    public void suscribirseAInsumos() {
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) { // Solo pedidos pendientes
                for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                    Insumo insumo = ingrediente.getInsumo();
                    insumo.desuscribir(this); // Evitar duplicados
                    insumo.subscribir(this);
                }
            }
        }
    }

    /**
     * Desuscribe el servicio de todos los insumos
     */
    public void desuscribirseDeInsumos() {
        for (Pedido pedido : pedidos) {
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                insumo.desuscribir(this);
            }
        }
    }

    /**
     * Verifica y elimina automáticamente pedidos que se quedaron sin stock
     * Llamado cuando se recibe notificación de cambio de stock
     */
    private void verificarYEliminarPedidosSinStock() {
        List<Pedido> pedidosAEliminar = new ArrayList<>();

        // SOLO verificar pedidos PENDIENTES (no confirmados)
        for (Pedido pedido : new ArrayList<>(pedidos)) { // Copia para evitar ConcurrentModificationException
            if (!pedidosConfirmados.contains(pedido)) { // Solo pedidos pendientes
                // Verificar si TODOS los ingredientes del pedido tienen stock suficiente
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
                // Crear mensaje antes de eliminar
                String mensaje = "Nos hemos quedado sin stock de "
                        + pedido.getItem().getNombre()
                        + " y no pudimos avisarte antes!";
                mensajesEliminacion.add(mensaje);

                // Eliminar pedido del servicio
                pedidos.remove(pedido);
                montoTotal -= pedido.calcularTotal();
            }

            // Notificar eliminaciones automáticas
            notificar(mensajesEliminacion);
            notificar(Evento.MONTO_ACTUALIZADO);
        }
    }
}

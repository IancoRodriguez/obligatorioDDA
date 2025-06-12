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

    public Servicio(Cliente cliente) {
        this.cliente = cliente;
        this.pedidos = new ArrayList<>();
<<<<<<< Updated upstream
=======
        this.pedidosConfirmados = new ArrayList<>();

>>>>>>> Stashed changes
    }

    public void suscribirseAItem(Item item) {
        item.subscribir(this);
    }

    // Modificar el método agregarPedido para suscribirse al ítem:
    public void agregarPedido(Pedido pedido) throws ServicioException {
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
<<<<<<< Updated upstream
        
        // NUEVO: Suscribirse a los insumos del pedido para detectar cuando se agoten
        suscribirseAInsumosDePedido(pedido);
        
        notificar(Evento.MONTO_ACTUALIZADO);
=======

        // NUEVO: Suscribirse al ítem para detectar cuando se quede sin stock
        suscribirseAItem(pedido.getItem());

        notificar(Observable.Evento.MONTO_ACTUALIZADO);
>>>>>>> Stashed changes
    }

    // Eliminar pedido del servicio 
    public void eliminarPedido(Pedido pedido) throws ServicioException {
        // 1. Valida si se puede eliminar (solo permitido para estado Confirmado)
        pedido.validarEliminacion();

<<<<<<< Updated upstream
        // 2. Reintegrar stock (ya que sabemos que está confirmado)
        reintegrarStock(pedido);

        // 3. Eliminar de la lista de pedidos
=======
        // NUEVO: Desuscribirse de los insumos del pedido
        desuscribirseDeInsumosDePedido(pedido);

        // Eliminar de la lista de pedidos
>>>>>>> Stashed changes
        pedidos.remove(pedido);

        // 4. Actualizar monto total
        montoTotal -= pedido.calcularTotal();
        notificar(Evento.MONTO_ACTUALIZADO);
    }

<<<<<<< Updated upstream
   public void confirmar() throws StockException {
    // Paso 1: Calcular requerimientos totales (optimizado)
    Map<Insumo, Integer> requerimientos = new HashMap<>();
    for (Pedido pedido : pedidos) {
        for (Ingrediente ing : pedido.getEstado().ingredientesParaConfirmar(pedido)) {
            requerimientos.merge(ing.getInsumo(), ing.getCantidad(), Integer::sum);
        }
    }

    // Paso 2: Validar stock global (sin consumir)
    for (Map.Entry<Insumo, Integer> entry : requerimientos.entrySet()) {
        Insumo insumo = entry.getKey();
        int totalRequerido = entry.getValue();
        if (insumo.getStock() < totalRequerido) {
            throw new StockException(
                "Stock insuficiente de " + insumo.getNombre() + 
                " (necesario: " + totalRequerido + 
                ", disponible: " + insumo.getStock() + ")"
            );
        }
    }

    // Paso 3: Consumir recursos (globalmente, no por pedido)
    for (Map.Entry<Insumo, Integer> entry : requerimientos.entrySet()) {
        Insumo insumo = entry.getKey();
        int cantidad = entry.getValue();
        insumo.consumirStock(cantidad); // ¡Implementa este método en Insumo!
    }

    // Paso 4: Cambiar estado de pedidos (sin consumir stock)
    for (Pedido pedido : pedidos) {
        pedido.getEstado().confirmar(pedido); // Nuevo método que no valida stock
    }
}

    public void validarStockPedidos() throws StockException {
=======
    // NUEVO: Método para suscribirse a los insumos de un pedido
    private void suscribirseAInsumosDePedido(Pedido pedido) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.subscribir(this);
        }
    }

    // NUEVO: Método para desuscribirse de los insumos de un pedido
    private void desuscribirseDeInsumosDePedido(Pedido pedido) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            insumo.desuscribir(this);
        }
    }

    // NUEVO: Implementación del patrón Observer para detectar insumos agotados
    @Override
    public void notificar(Observable origen, Object evento) {
        if (origen instanceof Insumo && evento instanceof Evento) {
            Evento tipoEvento = (Evento) evento;
            
            if (tipoEvento == Evento.INSUMO_AGOTADO) {
                eliminarPedidosPorInsumoAgotado((Insumo) origen);
            }
        }
    }

    // NUEVO: Elimina automáticamente pedidos cuando un insumo se agota
    private void eliminarPedidosPorInsumoAgotado(Insumo insumoAgotado) {
        List<Pedido> pedidosAEliminar = new ArrayList<>();
        List<String> mensajesEliminacion = new ArrayList<>();

        // Buscar pedidos pendientes (no confirmados) que usen este insumo
        for (Pedido pedido : pedidos) {
            if (!pedidosConfirmados.contains(pedido)) {
                // Verificar si el pedido usa el insumo agotado
                for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                    if (ingrediente.getInsumo().equals(insumoAgotado)) {
                        pedidosAEliminar.add(pedido);
                        
                        // Crear mensaje según la consigna
                        String mensaje = "Lo sentimos, nos hemos quedado sin stock de " 
                                + pedido.getItem().getNombre() 
                                + " por lo que lo hemos quitado el pedido del servicio";
                        mensajesEliminacion.add(mensaje);
                        break; // Solo necesitamos encontrar una coincidencia
                    }
                }
            }
        }

        // Eliminar pedidos y actualizar monto
        for (Pedido pedido : pedidosAEliminar) {
            pedidos.remove(pedido);
            montoTotal -= pedido.calcularTotal();
            desuscribirseDeInsumosDePedido(pedido);
        }

        // Notificar cambios si hubo eliminaciones
        if (!pedidosAEliminar.isEmpty()) {
            notificar(mensajesEliminacion); // Enviar mensajes de eliminación
            notificar(Evento.MONTO_ACTUALIZADO); // Actualizar monto
        }
    }

    public void confirmar() throws StockException {
        // Obtener pedidos pendientes de confirmación
        List<Pedido> pedidosPorConfirmar = new ArrayList<>();
>>>>>>> Stashed changes
        for (Pedido pedido : pedidos) {
<<<<<<< Updated upstream
            for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                int cantidadNecesariaTotal = 0;

                // Sumo todas las cantidades necesarias de este insumo en la lista de pedidos
                for (Pedido p : pedidos) {
                    for (Ingrediente ing : p.getItem().getIngredientes()) {
                        if (ing.getInsumo().equals(insumo)) {
                            cantidadNecesariaTotal += ing.getCantidad();
                        }
                    }
                }

                // Verifico si hay suficiente stock para este insumo
                if (insumo.getStock() < cantidadNecesariaTotal) {
                    throw new StockException("Nos hemos quedado sin stock de " + pedido.getItem().getNombre() + " y no pudimos avisarte antes!");
                }
            }
=======
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

    // CORREGIDO: Crear copia del stock disponible para TODOS los insumos que se van a consultar
    Map<Insumo, Integer> stockSimulado = new HashMap<>();
    
    // Inicializar con TODOS los insumos que aparecen en CUALQUIER pedido
    for (Pedido pedido : pedidosPorConfirmar) {
        for (Ingrediente ingrediente : pedido.getItem().getIngredientes()) {
            Insumo insumo = ingrediente.getInsumo();
            if (!stockSimulado.containsKey(insumo)) {
                stockSimulado.put(insumo, insumo.getStock());
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

        // CRÍTICO: Notificar que los pedidos cambiaron de estado
        // Esto fuerza la actualización de la tabla para mostrar los estados actualizados
        notificar(Evento.PEDIDOS_CONFIRMADOS); // Si existe este evento
        // O alternativamente:
        notificar("PEDIDOS_CONFIRMADOS"); // Como string genérico
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
>>>>>>> Stashed changes
        }
    }

    public List<Pedido> pedidosConStock() {
        List<Pedido> aux = new ArrayList();
        for (Pedido pedido : pedidos) {
            if (pedido.getItem().tieneStockDisponible()) {
                aux.add(pedido);
            }
        }
        return aux;
    }

    public List<Pedido> pedidosEliminados() {
        List<Pedido> aux = new ArrayList();
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
    
    
    public List<Pedido> mostrarPedidosPorUP(UnidadProcesadora UP) {
        List<Pedido> res = new ArrayList();
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
<<<<<<< Updated upstream
    
    
    
   
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
=======

    // Método para debugging
    public boolean estaPedidoConfirmado(Pedido pedido) {
        return pedidosConfirmados.contains(pedido);
    }

    @Override
    public void notificar(Observable origen, Object evento) {
        if (evento == Observable.Evento.ITEM_SIN_STOCK && origen instanceof Item) {
            Item itemSinStock = (Item) origen;
            eliminarPedidosPorItemSinStock(itemSinStock);
        }
    }

// Agregar este método privado:
    private void eliminarPedidosPorItemSinStock(Item itemSinStock) {
        List<Pedido> pedidosAEliminar = new ArrayList<>();

        // Encontrar pedidos no confirmados de este ítem
        for (Pedido pedido : pedidos) {
            if (pedido.getItem().equals(itemSinStock) && !pedidosConfirmados.contains(pedido)) {
                pedidosAEliminar.add(pedido);
            }
        }

        // Eliminar pedidos y mostrar mensajes
        for (Pedido pedido : pedidosAEliminar) {
            pedidos.remove(pedido);
            montoTotal -= pedido.calcularTotal();

            String mensaje = "Lo sentimos, nos hemos quedado sin stock de "
                    + itemSinStock.getNombre()
                    + " por lo que lo hemos quitado el pedido del servicio";

            // Notificar con el mensaje
            notificar(mensaje);
        }

        if (!pedidosAEliminar.isEmpty()) {
            notificar(Observable.Evento.MONTO_ACTUALIZADO);
        }
    }
}
>>>>>>> Stashed changes

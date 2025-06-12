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
    }

    // Agrega un pedido al servicio
    public void agregarPedido(Pedido pedido) throws ServicioException {
        pedidos.add(pedido);
        montoTotal += pedido.calcularTotal();
        
        // NUEVO: Suscribirse a los insumos del pedido para detectar cuando se agoten
        suscribirseAInsumosDePedido(pedido);
        
        notificar(Evento.MONTO_ACTUALIZADO);
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
}
>>>>>>> Stashed changes

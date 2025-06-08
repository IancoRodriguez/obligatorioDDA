package Dominio;

import Dominio.Estados.Confirmado;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Observer.Observable;
import Dominio.Pedido;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Servicio extends Observable {

    private Cliente cliente;
    private List<Pedido> pedidos;
    private double montoTotal;
    private final List<String> mensajesEliminados = new ArrayList<>();

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

    public void confirmar() {
        mensajesEliminados.clear();

        // Iteramos sobre una copia para evitar ConcurrentModification
        for (Pedido p : new ArrayList<>(pedidos)) {
            List<Ingrediente> ingredientes = p.getEstado().ingredientesParaConfirmar(p);
            if (ingredientes.isEmpty()) {
                continue; // no hay acción para pedidos ya confirmados o cancelados
            }

            boolean puedeConfirmar = true;
            for (Ingrediente ing : ingredientes) {
                if (!ing.tieneStockSuficiente()) {
                    puedeConfirmar = false;
                    break;
                }
            }

            if (puedeConfirmar) {
                // consume stock y cambia estado internamente
                try {
                    p.confirmar();
                } catch (StockException e) {
                    mensajesEliminados.add("Error inesperado al confirmar '"
                        + p.getItem().getNombre() + "': " + e.getMessage());
                }
            } else {
                // marcamos el ítem como no disponible para que el Observer lo oculte
                Item item = p.getItem();
                item.setDisponible(false);                   
                mensajesEliminados.add(
                    "No se pudo confirmar pedido de '" + item.getNombre() + "' por falta de stock; ítem deshabilitado"
                );
            }
        }
    }

            // Verificar stock disponible
            boolean puedeConfirmar = true;
            for (Ingrediente ing : porConfirmar) {
                if (!ing.tieneStockSuficiente()) {
                    puedeConfirmar = false;
                    break;
                }
            }

            if (puedeConfirmar) {
                // Si hay stock suficiente, confirmamos
                p.confirmar();
            } else {
                // --- CAMBIO AQUI: en lugar de eliminar el Pedido, marcamos el Item como no disponible ---
                // Antes: it.remove() o deje pendiente el Pedido
                // Ahora:
                p.getItem().setDisponible(false); // <<<<<<<<<<<< LÍNEA DE CAMBIO
                mensajesEliminados.add(
                    "No se pudo confirmar pedido de \"" + p.getItem().getNombre() +
                    "\" por falta de stock; ítem deshabilitado"
                );
            }
        }
    }
    
    
    
    
    
    

//    public void confirmar() throws StockException {
//        mensajesEliminados.clear();
//
//        // Validación global de stock (o se puede omitir si la confirmación individual ya chequea)
//        for (Pedido p1 : pedidos) {
//            for (Ingrediente ing1 : p1.getEstado().ingredientesParaConfirmar(p1)) {
//                Insumo ins = ing1.getInsumo();
//                int totalRequerido = 0;
//                for (Pedido p2 : pedidos) {
//                    for (Ingrediente ing2 : p2.getEstado().ingredientesParaConfirmar(p2)) {
//                        if (ing2.getInsumo().equals(ins)) {
//                            totalRequerido += ing2.getCantidad();
//                        }
//                    }
//                }
//                if (ins.getStock() < totalRequerido) {
//                    throw new StockException(
//                            "Stock insuficiente de " + ins.getNombre() + " (necesario " + totalRequerido + ", disponible " + ins.getStock() + ")"
//                    );
//                }
//            }
//        }
//
//        // Procesar cada pedido: confirmar o eliminar
//        Iterator<Pedido> it = pedidos.iterator();
//        while (it.hasNext()) {
//            Pedido p = it.next();
//            List<Ingrediente> porConfirmar = p.getEstado().ingredientesParaConfirmar(p);
//            if (!porConfirmar.isEmpty()) {
//                boolean puedeConfirmar = true;
//                for (Ingrediente ing : porConfirmar) {
//                    if (!ing.tieneStockSuficiente()) {
//                        puedeConfirmar = false;
//                        break;
//                    }
//                }
//                if (puedeConfirmar) {
//                    p.confirmar();
//                } else {
//                    String nombre = p.getItem().getNombre();
//                    it.remove();
//                    mensajesEliminados.add(
//                            "Lo sentimos, nos hemos quedado sin stock de “" + nombre + "” por lo que lo hemos quitado el pedido del servicio"
//                    );
//                }
//            }
//        }
//    }
    public boolean limpiarPedidosSinStock() {
        boolean huboCambios = false;
        mensajesEliminados.clear();

        Iterator<Pedido> it = pedidos.iterator();
        while (it.hasNext()) {
            Pedido p = it.next();
            List<Ingrediente> porConfirmar = p.getEstado().ingredientesParaConfirmar(p);

            if (!porConfirmar.isEmpty()) {
                for (Ingrediente ing : porConfirmar) {
                    if (!ing.tieneStockSuficiente()) {
                        String nombre = p.getItem().getNombre();
                        it.remove();
                        mensajesEliminados.add(
                                "Lo sentimos, nos hemos quedado sin stock de “" + nombre + "” por lo que lo hemos quitado el pedido del servicio"
                        );
                        huboCambios = true;
                        break; // con que falle uno, ya lo sacamos
                    }
                }
            }
        }

        return huboCambios;
    }

    /**
     * Mensajes generados al eliminar pedidos sin stock.
     */
    public List<String> getMensajesEliminados() {
        return mensajesEliminados;
    }

    public void validarStockPedidos() throws StockException {
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
//version sin map anterior 
//    public void confirmar() throws StockException {
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
//                            "Stock insuficiente de "
//                            + ins.getNombre()
//                            + " (necesario " + totalRequerido
//                            + ", disponible " + ins.getStock() + ")"
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
//este loco viejo anda pero borra todos los peddios para los que no alzanza el stock 
// public void confirmar() {
//        mensajesEliminados.clear();
//
//        Iterator<Pedido> it = pedidos.iterator();
//        while (it.hasNext()) {
//            Pedido p = it.next();
//            List<Ingrediente> porConfirmar = p.getEstado().ingredientesParaConfirmar(p);
//
//            // Solo pedidos pendientes (que consumen ingredientes)
//            if (porConfirmar.isEmpty()) {
//                continue;
//            }
//
//            // Chequear stock para este pedido
//            boolean puedeConfirmar = true;
//            for (Ingrediente ing : porConfirmar) {
//                if (!ing.tieneStockSuficiente()) {
//                    puedeConfirmar = false;
//                    break;
//                }
//            }
//
//            if (puedeConfirmar) {
//                try {
//                    // Consume stock y cambia estado internamente
//                    p.confirmar();
//                } catch (StockException e) {
//                    // En caso de error inesperado, elimina el pedido y guarda mensaje
//                    mensajesEliminados.add(
//                        "Error al confirmar pedido de " + p.getItem().getNombre() + ": " + e.getMessage()
//                    );
//                    it.remove();
//                }
//            } else {
//                // Stock insuficiente: elimina el pedido y registra el aviso
//                String nombre = p.getItem().getNombre();
//                it.remove();
//                mensajesEliminados.add(
//                    "Lo sentimos, nos hemos quedado sin stock de \"" + nombre + "\" y lo hemos quitado del servicio"
//                );
//            }
//        }
//    }

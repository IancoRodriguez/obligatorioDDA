/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.Controladores;

import Dominio.Categoria;
import Dominio.Excepciones.PedidoException;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
import Dominio.Ingrediente;
import Dominio.Item;
import Dominio.Menu;
import Dominio.Observer.Observable;
import Dominio.Observer.Observador;
import Dominio.Pedido;
import Dominio.Servicio;
import Servicios.Fachada;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ianco
 */
public class PedidosControlador implements Observador {

    private ClienteView vista;
    private Fachada fachada;
    private Menu menu;
    private Servicio servicioActual;

    public PedidosControlador(ClienteView vista) {
        this.vista = vista;
        this.fachada = Fachada.getInstancia();
        this.menu = Menu.getInstancia();
    }

    /**
     * Inicializa la vista cargando las categorías
     */
    public void inicializar() {
        cargarCategorias();
        vista.limpiarMensajesError();
    }

    /**
     * Carga todas las categorías disponibles en la vista
     */
    public void cargarCategorias() {
        try {
            List<Categoria> categorias = menu.getCategorias();
            vista.cargarCategorias(categorias);
        } catch (Exception ex) {
            vista.mostrarError("Error al cargar categorías: " + ex.getMessage());
        }
    }

    /**
     * Carga los items de la categoría seleccionada Solo muestra items con stock
     * disponible
     */
    public void cargarItemsPorCategoria() {
        try {
            Categoria categoriaSeleccionada = vista.getCategoriaSeleccionada();

            if (categoriaSeleccionada == null) {
                vista.cargarItems(new ArrayList<>());
                return;
            }

            List<Item> itemsDisponibles = new ArrayList<>();
            for (Item item : categoriaSeleccionada.getItems()) {
                if (item.tieneStockDisponible()) {
                    itemsDisponibles.add(item);
                }
            }

            vista.cargarItems(itemsDisponibles);
            vista.limpiarMensajesError();

        } catch (Exception ex) {
            vista.mostrarError("Error al cargar items: " + ex.getMessage());
        }
    }

    /**
     * Procesa el registro de un nuevo pedido
     */
    public void registrarPedido() {
        try {
            // Validaciones previas
            Servicio servicioActual = vista.getServicioActual();
            if (servicioActual == null) {
                throw new ServicioException("Debe identificarse antes de agregar un pedido");
            }

            Item itemSeleccionado = vista.getItemSeleccionado();
            if (itemSeleccionado == null) {
                throw new PedidoException("Debe seleccionar un item");
            }

            String comentario = vista.getComentario();

            // Crear y agregar el pedido
            Pedido nuevoPedido = new Pedido(itemSeleccionado, comentario, servicioActual);
            servicioActual.agregarPedido(nuevoPedido);

            // Actualizar la vista
            actualizarVistaPedidos(servicioActual);
            vista.limpiarComentario();
            vista.limpiarMensajesError();

            // Recargar items por si cambió el stock
            cargarItemsPorCategoria();

        } catch (StockException ex) {
            vista.mostrarError("Sin stock disponible: " + ex.getMessage());
        } catch (ServicioException ex) {
            vista.mostrarError("Error de servicio: " + ex.getMessage());
        } catch (PedidoException ex) {
            vista.mostrarError("Error en pedido: " + ex.getMessage());
        } catch (Exception ex) {
            vista.mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * Procesa la eliminación de un pedido seleccionado
     */
    public void eliminarPedido() {
        try {
            Servicio servicioActual = vista.getServicioActual();
            if (servicioActual == null) {
                throw new ServicioException("Debe identificarse antes de eliminar pedidos");
            }

            int indiceSeleccionado = vista.getPedidoSeleccionadoIndex();
            if (indiceSeleccionado == -1) {
                throw new PedidoException("Debe seleccionar un pedido a eliminar");
            }

            List<Pedido> pedidos = servicioActual.getPedidos();
            if (indiceSeleccionado >= pedidos.size()) {
                throw new PedidoException("Pedido seleccionado no válido");
            }

            Pedido pedidoAEliminar = pedidos.get(indiceSeleccionado);
            servicioActual.eliminarPedido(pedidoAEliminar);

            // Actualizar la vista
            actualizarVistaPedidos(servicioActual);
            vista.limpiarMensajesError();

            // Recargar items por si se liberó stock
            cargarItemsPorCategoria();

        } catch (ServicioException ex) {
            vista.mostrarError("Error de servicio: " + ex.getMessage());
        } catch (PedidoException ex) {
            vista.mostrarError("Error en pedido: " + ex.getMessage());
        } catch (Exception ex) {
            vista.mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    public void confirmarPedidos() {
        try {
            Servicio servicioActual = vista.getServicioActual();
            if (servicioActual == null) {
                throw new ServicioException("Debe identificarse antes de confirmar pedidos");
            }

            if (servicioActual.getPedidos().isEmpty()) {
                vista.mostrarError("No hay pedidos para confirmar");
                return;
            }

            // Verificar si hay pedidos pendientes
            List<Pedido> pedidosPendientes = new ArrayList<>();
            for (Pedido p : servicioActual.getPedidos()) {
                if (!servicioActual.estaPedidoConfirmado(p)) {
                    pedidosPendientes.add(p);
                }
            }

            if (pedidosPendientes.isEmpty()) {
                vista.mostrarError("No hay pedidos pendientes para confirmar");
                return;
            }

            // Llamar al método confirmar del servicio y obtener resultado
            Servicio.ConfirmacionResult resultado = servicioActual.confirmar();
            this.servicioActual.subscribir(this);

            // Actualizar vista SIEMPRE
            actualizarVistaPedidos(servicioActual);
            cargarItemsPorCategoria();

            // Mostrar mensaje basado en el resultado
            mostrarResultadoConfirmacion(resultado);

        } catch (StockException ex) {
            vista.mostrarError("Error de stock: " + ex.getMessage());
            cargarItemsPorCategoria();
            Servicio servicioActual = vista.getServicioActual();
            if (servicioActual != null) {
                actualizarVistaPedidos(servicioActual);
            }
        } catch (ServicioException ex) {
            vista.mostrarError("Error de servicio: " + ex.getMessage());
        } catch (Exception ex) {
            vista.mostrarError("Error inesperado: " + ex.getMessage());
        }
    }

    /**
     * MÉTODO NUEVO: Muestra el resultado de la confirmación basado en
     * ConfirmacionResult
     */
    private void mostrarResultadoConfirmacion(Servicio.ConfirmacionResult resultado) {
        StringBuilder mensaje = new StringBuilder();
        boolean hayEliminados = resultado.hayPedidosEliminados();
        boolean hayConfirmados = resultado.hayPedidosConfirmados();

        // Mostrar pedidos eliminados
        if (hayEliminados) {
            mensaje.append("Pedidos eliminados por falta de stock:\n");
            for (Pedido pedido : resultado.getPedidosEliminados()) {
                mensaje.append("• Nos hemos quedado sin stock de ")
                        .append(pedido.getItem().getNombre())
                        .append(" y no pudimos avisarte antes!\n");
            }
            mensaje.append("\n");
        }

        // Mostrar pedidos confirmados
        if (hayConfirmados) {
            mensaje.append(" Pedidos confirmados exitosamente:\n");
            for (Pedido pedido : resultado.getPedidosConfirmados()) {
                mensaje.append("• ").append(pedido.getItem().getNombre()).append("\n");
            }
        }

        // Mostrar mensaje apropiado
        if (hayConfirmados && hayEliminados) {
            vista.mostrarMensajeExito("Confirmación parcial:\n\n" + mensaje.toString());
        } else if (hayConfirmados && !hayEliminados) {
            vista.mostrarMensajeExito(mensaje.toString());
        } else if (hayEliminados && !hayConfirmados) {
            vista.mostrarError("No se pudieron confirmar los pedidos:\n\n" + mensaje.toString());
        }
    }

    /**
     * CORREGIDO: Analiza el resultado de la confirmación comparando estado
     * inicial vs final (Mantener este método por si quieres usarlo con la
     * versión original)
     */
    private void analizarYMostrarResultadoConfirmacion(List<String> nombresIniciales, Servicio servicio) {
        // Obtener estado final
        List<Pedido> pedidosFinales = servicio.getPedidos();
        List<String> nombresFinales = new ArrayList<>();
        List<String> nombresConfirmados = new ArrayList<>();

        for (Pedido p : pedidosFinales) {
            String nombre = p.getItem().getNombre();
            nombresFinales.add(nombre);

            // CORREGIDO: Verificar si fue confirmado usando el método del servicio
            if (servicio.estaPedidoConfirmado(p)) {
                // Solo agregar si estaba en la lista inicial (es decir, fue recién confirmado)
                if (nombresIniciales.contains(nombre)) {
                    nombresConfirmados.add(nombre);
                }
            }
        }

        // Determinar cuáles fueron eliminados
        List<String> nombresEliminados = new ArrayList<>(nombresIniciales);
        nombresEliminados.removeAll(nombresFinales);

        // Construir y mostrar mensaje
        StringBuilder mensaje = new StringBuilder();
        boolean hayEliminados = !nombresEliminados.isEmpty();
        boolean hayConfirmados = !nombresConfirmados.isEmpty();

        if (hayEliminados) {
            mensaje.append("Pedidos eliminados por falta de stock:\n");
            for (String nombre : nombresEliminados) {
                mensaje.append("• Nos hemos quedado sin stock de ").append(nombre)
                        .append(" y no pudimos avisarte antes!\n");
            }
            mensaje.append("\n");
        }

        if (hayConfirmados) {
            mensaje.append("Pedidos confirmados exitosamente:\n");
            for (String nombre : nombresConfirmados) {
                mensaje.append("• ").append(nombre).append("\n");
            }
        }

        // Mostrar mensaje apropiado
        if (hayConfirmados && hayEliminados) {
            // Caso mixto: algunos confirmados, algunos eliminados
            vista.mostrarMensajeExito("Confirmación parcial:\n\n" + mensaje.toString());
        } else if (hayConfirmados && !hayEliminados) {
            // Solo confirmaciones exitosas
            vista.mostrarMensajeExito(mensaje.toString());
        } else if (hayEliminados && !hayConfirmados) {
            // Solo eliminaciones
            vista.mostrarError("No se pudieron confirmar los pedidos:\n\n" + mensaje.toString());
        }
    }

    // MEJORAR el método para manejar mensajes automáticos de eliminación
    private void handlePedidosEliminadosConMensajes(Observable origen, List<String> mensajes) {
        if (origen == vista.getServicioActual()) {
            // Solo mostrar estos mensajes si NO estamos en proceso de confirmación
            // (para evitar mensajes duplicados)
            StringBuilder mensajeCompleto = new StringBuilder();
            mensajeCompleto.append("Pedidos eliminados automáticamente:\n\n");

            for (String mensaje : mensajes) {
                mensajeCompleto.append("• ").append(mensaje).append("\n");
            }

            mensajeCompleto.append(
                    "\nEstos pedidos fueron eliminados porque otros clientes consumieron el stock mientras navegabas.");

            vista.mostrarError(mensajeCompleto.toString());

            // Actualizar la tabla inmediatamente
            actualizarVistaPedidos(vista.getServicioActual());
            cargarItemsPorCategoria();
        }
    }
    // /**
    // * Muestra los mensajes apropiados según el resultado de la confirmación
    // */
    // private void mostrarResultadoConfirmacion(Servicio.ConfirmacionResult
    // resultado) {
    // StringBuilder mensaje = new StringBuilder();
    //
    // // Mostrar pedidos eliminados por falta de stock
    // if (resultado.hayPedidosEliminados()) {
    // mensaje.append("Pedidos eliminados por falta de stock:\n");
    // for (Pedido pedido : resultado.getPedidosEliminados()) {
    // mensaje.append("• Nos hemos quedado sin stock de ")
    // .append(pedido.getItem().getNombre())
    // .append(" y no pudimos avisarte antes!\n");
    // }
    // mensaje.append("\n");
    // }
    //
    // // Mostrar resultado de confirmación
    // if (resultado.isConfirmacionExitosa()) {
    // if (resultado.hayPedidosEliminados()) {
    // mensaje.append("Los pedidos restantes fueron confirmados exitosamente.");
    // } else {
    // mensaje.append("Pedidos confirmados exitosamente.");
    // }
    //
    // // Mostrar como mensaje de éxito si hay confirmaciones
    // vista.mostrarMensajeExito(mensaje.toString());
    // } else {
    // if (resultado.hayPedidosEliminados()) {
    // mensaje.append("No quedaron pedidos para confirmar después de eliminar los
    // que no tenían stock.");
    // vista.mostrarError(mensaje.toString());
    // }
    // }
    // }

    /**
     * Actualiza la vista con los pedidos y monto del servicio actual
     */
    private void actualizarVistaPedidos(Servicio servicio) {
        if (servicio != null) {
            vista.actualizarTablaPedidos(servicio.getPedidos());
            vista.actualizarMontoTotal(servicio.getMontoTotal());
        }
    }

    public void cerrarSesion() {
        if (this.servicioActual != null) {
            this.servicioActual.desuscribirseDeInsumos();
            this.servicioActual.desuscribir(this);
        }

        // Limpiar vista
        limpiarVista();
        this.servicioActual = null;
    }

    public void limpiarVista() {
        vista.actualizarTablaPedidos(new ArrayList<>());
        vista.actualizarMontoTotal(0.0);
        vista.limpiarComentario();
        vista.limpiarMensajesError();
    }

    /**
     * Maneja el cambio de categoría seleccionada MODIFICADO: Ahora incluye
     * suscripción a observers
     */
    public void onCategoriaSeleccionada() {
        Categoria categoria = vista.getCategoriaSeleccionada();

        if (categoria != null) {
            List<Item> itemsDisponibles = new ArrayList<>();

            for (Item item : categoria.getItems()) {
                // El CONTROLADOR se suscribe a cada item
                item.desuscribir(this); // Evitar duplicados
                item.subscribir(this);

                if (item.tieneStockDisponible()) {
                    itemsDisponibles.add(item);
                }
            }

            vista.cargarItems(itemsDisponibles);
        } else {
            vista.cargarItems(new ArrayList<>());
        }
    }

    /**
     * Actualiza la vista cuando el servicio cambia (por ejemplo, después del
     * login) CORREGIDO: Solo un método onServicioActualizado
     */
    public void onServicioActualizado() {
        // Limpiar suscripciones del servicio anterior
        if (this.servicioActual != null) {
            this.servicioActual.desuscribir(this);
            this.servicioActual.desuscribirseDeInsumos(); // NUEVO
        }

        // Actualizar referencia al servicio actual
        this.servicioActual = vista.getServicioActual();

        // Configurar nuevo servicio
        if (servicioActual != null) {
            servicioActual.subscribir(this);
            servicioActual.suscribirseAInsumos(); // NUEVO - Auto-suscribirse a insumos
            actualizarVistaPedidos(servicioActual);
        } else {
            limpiarVista();
        }

        // Recargar categorías y items
        cargarCategorias();
    }

    /**
     * AGREGADO: Método para suscribirse a una lista de items
     */
    public void suscribirseAItems(List<Item> items) {
        for (Item item : items) {
            item.desuscribir(this); // Evitar duplicados
            item.subscribir(this);
        }
    }

    @Override
    public void notificar(Observable origen, Object evento) {
        // CASO 1: Evento es del tipo Observable.Evento
        if (evento instanceof Observable.Evento) {
            Observable.Evento tipoEvento = (Observable.Evento) evento;

            switch (tipoEvento) {
                case ITEM_ACTUALIZADO:
                    handleItemActualizado((Item) origen);
                    break;
                case MONTO_ACTUALIZADO:
                    handleMontoActualizado(origen);
                    break;
                case PEDIDOS_ELIMINADOS_POR_STOCK:
                    handlePedidosEliminadosSinMensajes(origen);
                    break;
                case PEDIDO_CAMBIO_ESTADO:
                    actualizarVistaPedidos(servicioActual);
                    break;
            }
        } // CASO 2: El evento es una lista de mensajes (viene del método
          // procesarEliminacionesAutomaticas)
        else if (evento instanceof List) {
            try {
                @SuppressWarnings("unchecked")
                List<String> mensajes = (List<String>) evento;
                handlePedidosEliminadosConMensajes(origen, mensajes);
            } catch (ClassCastException e) {
                // Si no es una lista de strings, manejo genérico
                handlePedidosEliminadosSinMensajes(origen);
            }
        } // CASO 3: Evento de pedidos confirmados (string)
        else if (evento instanceof String) {
            String eventoStr = (String) evento;
            if ("PEDIDOS_CONFIRMADOS".equals(eventoStr)) {
                handlePedidosConfirmados(origen);
            }
        } // CASO 4: Cualquier otro tipo de evento relacionado con pedidos eliminados
        else {
            handlePedidosEliminadosSinMensajes(origen);
        }
    }

    /**
     * Maneja la confirmación de pedidos - actualiza la tabla para mostrar
     * estados
     */
    private void handlePedidosConfirmados(Observable origen) {
        if (origen == vista.getServicioActual()) {
            // Actualizar la tabla para mostrar los nuevos estados
            actualizarVistaPedidos(vista.getServicioActual());
            cargarItemsPorCategoria();
        }
    }

    // /**
    // * Maneja eliminaciones cuando tenemos los mensajes específicos
    // */
    // private void handlePedidosEliminadosConMensajes(Observable origen,
    // List<String> mensajes) {
    // if (origen == vista.getServicioActual()) {
    // // Mostrar cada mensaje de eliminación
    // for (String mensaje : mensajes) {
    // vista.mostrarError(mensaje);
    // }
    //
    // // CRÍTICO: Actualizar la tabla inmediatamente
    // actualizarVistaPedidos(vista.getServicioActual());
    // cargarItemsPorCategoria();
    // }
    // }
    /**
     * Maneja eliminaciones cuando NO tenemos mensajes específicos
     */
    private void handlePedidosEliminadosSinMensajes(Observable origen) {
        if (origen == vista.getServicioActual()) {
            // Actualizar la tabla sin mostrar mensajes específicos
            actualizarVistaPedidos(vista.getServicioActual());
            cargarItemsPorCategoria();
        }
    }

    private void handleItemActualizado(Item item) {
        if (item.tieneStockDisponible()) {
            vista.actualizarItemEnLista(item);
        } else {
            vista.removerItemDeLista(item);
        }
    }

    private void handleMontoActualizado(Observable origen) {
        if (origen == vista.getServicioActual()) {
            vista.actualizarMontoTotal(vista.getServicioActual().getMontoTotal());
        }
    }

    // Método para manejar notificaciones delegadas (mantener temporalmente)
    public void manejarNotificacion(Observable origen, Object evento) {
        // Redirigir al método notificar
        notificar(origen, evento);
    }

}

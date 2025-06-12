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
import Dominio.Insumo;
import Dominio.Item;
import Dominio.Menu;
import Dominio.Observer.Observable;
import Dominio.Observer.Observable.Evento;
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

            // Guardar estado inicial
            int pedidosIniciales = servicioActual.getPedidos().size();

            // Llamar al método confirmar del servicio
            servicioActual.confirmar();

            // CRÍTICO: SIEMPRE actualizar la vista después de confirmar
            // independientemente de si hubo eliminaciones o no
            actualizarVistaPedidos(servicioActual);
            cargarItemsPorCategoria();

            // Verificar resultado y mostrar mensaje apropiado
            int pedidosFinales = servicioActual.getPedidos().size();

            if (pedidosFinales > 0) {
                // Hay pedidos confirmados exitosamente
                vista.mostrarMensajeExito("Pedidos confirmados exitosamente");
            } else if (pedidosIniciales > pedidosFinales) {
                // Solo había pedidos que se eliminaron por falta de stock
                vista.mostrarError("No se pudieron confirmar los pedidos por falta de stock");
            }

        } catch (StockException ex) {
            // Este caso ya no debería ocurrir con la nueva lógica
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
     * Actualiza la vista con los pedidos y monto del servicio actual
     */
    private void actualizarVistaPedidos(Servicio servicio) {
        if (servicio != null) {
            vista.actualizarTablaPedidos(servicio.getPedidos());
            vista.actualizarMontoTotal(servicio.getMontoTotal());
        }
    }

    /**
     * Limpia todos los datos de pedidos en la vista
     */
    public void limpiarVista() {
        vista.actualizarTablaPedidos(new ArrayList<>());
        vista.actualizarMontoTotal(0.0);
        vista.limpiarComentario();
        vista.limpiarMensajesError();
    }

    /**
     * Maneja el cambio de categoría seleccionada
     */
    public void onCategoriaSeleccionada() {
        Categoria categoria = vista.getCategoriaSeleccionada();

        if (categoria != null) {
            List<Item> itemsDisponibles = new ArrayList<>();

            for (Item item : categoria.getItems()) {
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
     * CRÍTICO: Este método se ejecuta cuando se actualiza el servicio (ej:
     * después del login)
     */
    public void onServicioActualizado() {
        this.servicioActual = vista.getServicioActual();

        // Limpiar suscripciones anteriores si existía un servicio previo
        if (this.servicioActual != null) {
            this.servicioActual.desuscribir(this);
        }

        // Suscribirse al nuevo servicio si existe
        if (servicioActual != null) {
            servicioActual.subscribir(this);
            actualizarVistaPedidos(servicioActual);
        } else {
            limpiarVista();
        }

        // Recargar categorías y items
        cargarCategorias();
    }

    @Override
    public void notificar(Observable origen, Object evento) {
        // CASO 1: Lista de mensajes de eliminación (lo más importante)
        if (evento instanceof List) {
            try {
                @SuppressWarnings("unchecked")
                List<String> mensajes = (List<String>) evento;
                handlePedidosEliminadosConMensajes(origen, mensajes);
            } catch (ClassCastException e) {
                // Si no es una lista de strings, manejo genérico
                handleEventoGenerico(origen, evento);
            }
        } // CASO 2: Evento de tipo Evento enum
        else if (evento instanceof Evento) {
            Evento tipoEvento = (Evento) evento;
            switch (tipoEvento) {
                case MONTO_ACTUALIZADO:
                    handleMontoActualizado(origen);
                    break;
                case STOCK_ACTUALIZADO:
                    cargarItemsPorCategoria(); // Recargar items cuando cambia stock
                    break;
                default:
                    handleEventoGenerico(origen, evento);
                    break;
            }
        } // CASO 3: String de confirmación
        else if (evento instanceof String) {
            String eventoStr = (String) evento;
            if ("PEDIDOS_CONFIRMADOS".equals(eventoStr)) {
                handlePedidosConfirmados(origen);
            }
        } // CASO 4: Cualquier otro evento
        else {
            handleEventoGenerico(origen, evento);
        }
    }

    /**
     * CRÍTICO: Maneja eliminaciones automáticas por stock agotado
     */
    private void handlePedidosEliminadosConMensajes(Observable origen, List<String> mensajes) {
        if (origen == vista.getServicioActual()) {
            // Mostrar cada mensaje de eliminación automática
            for (String mensaje : mensajes) {
                vista.mostrarError(mensaje);
            }

            // CRÍTICO: Actualizar la vista inmediatamente
            actualizarVistaPedidos(vista.getServicioActual());
            cargarItemsPorCategoria();
        }
    }

    /**
     * Maneja la confirmación de pedidos
     */
    private void handlePedidosConfirmados(Observable origen) {
        if (origen == vista.getServicioActual()) {
            actualizarVistaPedidos(vista.getServicioActual());
            cargarItemsPorCategoria();
        }
    }

    /**
     * Maneja actualización de monto
     */
    private void handleMontoActualizado(Observable origen) {
        if (origen == vista.getServicioActual()) {
            vista.actualizarMontoTotal(vista.getServicioActual().getMontoTotal());
        }
    }

    /**
     * Manejo genérico para otros eventos
     */
    private void handleEventoGenerico(Observable origen, Object evento) {
        if (origen == vista.getServicioActual()) {
            actualizarVistaPedidos(vista.getServicioActual());
            cargarItemsPorCategoria();
        }
    }

    public void suscribirseAItems(List<Item> items) {
        // Primero desuscribirse de insumos previos si los había
        desuscribirseDeInsumosPrevios();

        // Suscribirse a todos los insumos de los items actuales
        for (Item item : items) {
            for (Ingrediente ingrediente : item.getIngredientes()) {
                Insumo insumo = ingrediente.getInsumo();
                insumo.subscribir(this);
            }
        }
    }

    /**
     * NUEVO: Se desuscribe de insumos previos para evitar memory leaks
     */
    private void desuscribirseDeInsumosPrevios() {
        // Esta implementación depende de cómo tengas organizado el Menu
        // Por ahora, una implementación simple sería:
        try {
            List<Categoria> categorias = menu.getCategorias();
            for (Categoria categoria : categorias) {
                for (Item item : categoria.getItems()) {
                    for (Ingrediente ingrediente : item.getIngredientes()) {
                        Insumo insumo = ingrediente.getInsumo();
                        insumo.desuscribir(this);
                    }
                }
            }
        } catch (Exception e) {
            // Manejo silencioso del error
        }
    }

}

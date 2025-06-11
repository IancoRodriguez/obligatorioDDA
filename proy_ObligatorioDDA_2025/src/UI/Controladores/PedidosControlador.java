/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.Controladores;

import Dominio.Categoria;
import Dominio.Excepciones.PedidoException;
import Dominio.Excepciones.ServicioException;
import Dominio.Excepciones.StockException;
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
    private Servicio servicioActual; // AGREGADO: Declarar la variable

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

    /**
     * Procesa la confirmación de todos los pedidos del servicio
     */
    public void confirmarPedidos() {
        try {
            Servicio servicioActual = vista.getServicioActual();
            if (servicioActual == null) {
                throw new ServicioException("Debe identificarse antes de confirmar pedidos");
            }

            // Verificar que hay pedidos para confirmar
            if (servicioActual.getPedidos().isEmpty()) {
                vista.mostrarError("No hay pedidos para confirmar");
                return;
            }

            servicioActual.confirmar();
            actualizarVistaPedidos(servicioActual);
            vista.limpiarMensajesError();

            // Recargar items por si cambió el stock después de confirmar
            cargarItemsPorCategoria();

            // Mensaje de éxito
            vista.mostrarError("Pedidos confirmados exitosamente");

        } catch (StockException ex) {
            vista.mostrarError("Error de stock: " + ex.getMessage());
            // Recargar la vista para mostrar el estado actual
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
        this.servicioActual = vista.getServicioActual();

        // Limpiar suscripciones anteriores si existía un servicio previo
        if (this.servicioActual != null) {
            this.servicioActual.desuscribir(this);
        }

        // Suscribirse al nuevo servicio
        if (servicioActual != null) {
            servicioActual.subscribir(this);
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

    // IMPLEMENTAR Observer en el controlador
    @Override
    public void notificar(Observable origen, Object evento) {
        if (evento instanceof Observable.Evento) {
            Observable.Evento tipoEvento = (Observable.Evento) evento;

            switch (tipoEvento) {
                case ITEM_ACTUALIZADO:
                    handleItemActualizado((Item) origen);
                    break;
                case MONTO_ACTUALIZADO:
                    handleMontoActualizado(origen);
                    break;
            }
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package UI.Controladores;

import Dominio.Categoria;
import Dominio.Dispositivo;
import Dominio.Excepciones.DispositivoException;
import Dominio.Item;
import Dominio.Pedido;
import Dominio.Servicio;
import java.util.List;

/**
 *
 * @author ianco
 */
 
public interface ClienteView {
    
   // === MÉTODOS COMUNES ===
    void mostrarError(String mensaje);
    void limpiarMensajesError();
    Dispositivo getDispositivo();
    
    // === LOGIN ===
    String getUsuario();
    String getContrasena();
    void setLogueado(boolean estado);
    void limpiarCampos();
    void limpiarSesionDispositivo() throws DispositivoException;
    
    // === PEDIDOS ===
    Item getItemSeleccionado();
    Categoria getCategoriaSeleccionada();
    String getComentario();
    int getPedidoSeleccionadoIndex();
    Servicio getServicioActual();
    
    void cargarCategorias(List<Categoria> categorias);
    void cargarItems(List<Item> items);
    void actualizarTablaPedidos(List<Pedido> pedidos);
    void actualizarMontoTotal(double monto);
    void limpiarComentario();
    
    // === FINALIZAR SERVICIO ===
    void mostrarResumenPago(String resumen);
    void cambiarTextoBotonFinalizar(String texto);
    void mostrarMensajeExito(String mensaje);
    void limpiarInterfaz();
    void reiniciarFlujo();
    void cerrarSesion();
    
    // === NOTIFICACIONES (manejadas por controlador) ===
    void actualizarItemEnLista(Item item);
    void removerItemDeLista(Item item); // AGREGADO: método faltante
    void refrescarListaItems();
}

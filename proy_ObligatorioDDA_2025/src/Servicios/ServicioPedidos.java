package Servicios;

import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Excepciones.SinStockException;
import Dominio.Ingrediente;
import Dominio.Insumo;
import Dominio.Item;
import Dominio.Pedido;
import Dominio.Servicio;
import java.util.ArrayList;
import java.util.List;

public class ServicioPedidos {

    private List<Pedido> pedidos;

    public ServicioPedidos() {
        this.pedidos = new ArrayList();

    }

    public void agregarPedido(Pedido pedido) {
        this.pedidos.add(pedido);
    }

    public void eliminarPedido(Pedido pedido) {
       this.pedidos.remove(pedido);
    }

    public List<Pedido> getPedidosPendientesUP(String nombreUP) {
        List<Pedido> pPendientes = new ArrayList();

        for (Pedido p : pedidos) {
            if (p.getEstado() == "Pendiente") {
                pPendientes.add(p);
            }
        }

        return pPendientes;
    }

    // ======================
    // Métodos auxiliares 
    // ======================
    private void validarEstadoServicio(Servicio servicio) {
        if (!servicio.getEstado().equals("En curso")) {
            throw new IllegalStateException("No se pueden agregar pedidos a un servicio finalizado");
        }
    }

    private void validarEstadoPedido(Pedido pedido) {
        if (pedido.getEstado().equals("Confirmado")) {
            //todo 
            // throw new PedidoException("No se puede eliminar un pedido confirmado");
        }

    }

    // ======================
    // Gestión de servicios
    // ======================
    public Servicio iniciarServicio(Cliente cliente, Dispositivo dispositivo) {
        if (dispositivo.estaOcupado()) {
            throw new IllegalStateException("Dispositivo ocupado");
        }

        Servicio servicio = new Servicio(cliente);
        dispositivo.ocupar(servicio); // Dispositivo almacena la referencia al Servicio
        return servicio;
    }

    public void finalizarServicio(Dispositivo dispositivo) {
        Servicio servicio = dispositivo.getServicioActivo();
        servicio.setEstado("Finalizado");
        dispositivo.liberar(); // Elimina la referencia al Servicio
    }

    public void confirmarServicio(Servicio servicio) throws SinStockException {
        // sPedidos.validarStock(servicio.getPedidos());
        // sPedidos.asignarUnidadesProcesadoras(servicio.getPedidos());
        servicio.setEstado("Confirmado");
    }

    // ======================
    // Getters
    // ======================
    public void cambiarEstado(Pedido p) {
        p.setEstado("Pedido en curso");
    }

}

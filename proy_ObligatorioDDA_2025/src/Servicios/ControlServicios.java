package Servicios;

import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Excepciones.SinStockException;
import Dominio.Servicio;
import java.util.ArrayList;
import java.util.List;

public class ControlServicios {

    private ServicioPedidos sPedidos;
    private List<Servicio> serviciosActivos;
    private List<Servicio> serviciosHistoricos;

    public ControlServicios(ServicioPedidos sPedidos) {
        this.sPedidos = sPedidos;
        this.serviciosActivos = new ArrayList<>();
        this.serviciosHistoricos = new ArrayList<>();
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
    public List<Servicio> getServiciosActivos() {
        return serviciosActivos;
    }

    public List<Servicio> getServiciosHistoricos() {
        return serviciosHistoricos;
    }
}

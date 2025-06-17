
package UI.Controladores;

import Dominio.Excepciones.ServicioException;
import Dominio.Servicio;


public class FinalizarServicioControlador {
    
    private ClienteView vista;
    private EstadoFinalizacion estadoActual;
    
    // Estados del proceso de finalización
    private enum EstadoFinalizacion {
        INICIAL,
        MOSTRANDO_RESUMEN,
        CONFIRMANDO_PAGO
    }
    
    public FinalizarServicioControlador(ClienteView vista) {
        this.vista = vista;
        this.estadoActual = EstadoFinalizacion.INICIAL;
    }
    
    
    public void procesarFinalizarServicio() {
        try {
            Servicio servicioActual = vista.getServicioActual();
            
            if (servicioActual == null) {
                throw new ServicioException("No hay servicio activo");
            }
            
            switch (estadoActual) {
                case INICIAL:
                    mostrarResumen(servicioActual);
                    break;
                case MOSTRANDO_RESUMEN:
                    confirmarPago(servicioActual);
                    break;
                default:
                    reiniciarProceso();
                    break;
            }
            
        } catch (ServicioException ex) {
            vista.mostrarError(ex.getMessage());
            reiniciarProceso();
        }
    }
    
   
    private void mostrarResumen(Servicio servicio) {
        // Obtener costo inicial
        double costoInicial = servicio.getMontoTotal();
        servicio.aplicarBeneficiosCliente();
        // Obtener costo FINAL después del descuento
        double costoFinal = servicio.getMontoTotal();
        String mensajeBeneficio = servicio.getCliente().getTipoCliente().getMensajeBeneficio();
        String resumen = construirResumenPago(mensajeBeneficio, costoFinal);
        
        // Actualizar la vista
        vista.mostrarResumenPago(resumen);
        vista.cambiarTextoBotonFinalizar("CONFIRMAR PAGO");
        estadoActual = EstadoFinalizacion.MOSTRANDO_RESUMEN;
    }
    
   
    private void confirmarPago(Servicio servicio) throws ServicioException {
        servicio.finalizar();
        // Mostrar mensaje de éxito
        String mensajeExito = "SERVICIO FINALIZADO";
        vista.mostrarMensajeExito(mensajeExito);
        
        vista.cerrarSesion();
        vista.limpiarInterfaz();
        
        // 4. Reiniciar estado
        estadoActual = EstadoFinalizacion.INICIAL;
    }
    
    
    private String construirResumenPago(String mensajeBeneficio, double costoFinal) {
        return "Resumen de Pago\n"
             + "---------------\n"
             + "Beneficio:  " + mensajeBeneficio + "\n"
             + "---------------\n"
             + "Total:      " + formatoMoneda(costoFinal);
    }
    
   
    private String formatoMoneda(double valor) {
        return String.format("$%,.2f", valor);
    }
    
    
    public void reiniciarProceso() {
        estadoActual = EstadoFinalizacion.INICIAL;
        vista.reiniciarFlujo();
    }
    
    
    public EstadoFinalizacion getEstadoActual() {
        return estadoActual;
    }
    
}
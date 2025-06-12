/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.Controladores;

import Dominio.Excepciones.ServicioException;
import Dominio.Servicio;

/**
 *
 * @author ianco
 */
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
    
    /**
     * Procesa el clic en el botón de finalizar servicio
     */
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
    
    /**
     * Muestra el resumen de pago y cambia al estado de confirmación
     */
    private void mostrarResumen(Servicio servicio) {
        // 1. Obtener costo inicial
        double costoInicial = servicio.getMontoTotal();
        
        // 2. Aplicar beneficio (esto modifica el estado del servicio)
        servicio.aplicarBeneficiosCliente();
        
        // 3. Obtener costo FINAL después del descuento
        double costoFinal = servicio.getMontoTotal();
        
        // 4. Obtener mensaje de beneficio del tipo de cliente
        String mensajeBeneficio = servicio.getCliente().getTipoCliente().getMensajeBeneficio();
        
        // 5. Construir el resumen
        String resumen = construirResumenPago(mensajeBeneficio, costoFinal);
        
        // 6. Actualizar la vista
        vista.mostrarResumenPago(resumen);
        vista.cambiarTextoBotonFinalizar("CONFIRMAR PAGO");
        
        // 7. Cambiar estado
        estadoActual = EstadoFinalizacion.MOSTRANDO_RESUMEN;
    }
    
    /**
     * Confirma el pago y finaliza el servicio
     */
    private void confirmarPago(Servicio servicio) throws ServicioException {
        // 1. Finalizar el servicio
        servicio.finalizar();
        
        // 2. Mostrar mensaje de éxito
        String mensajeExito = "<html><div style='color: green; text-align: center;'>"
                            + "✅ <b>SERVICIO FINALIZADO</b></div></html>";
        vista.mostrarMensajeExito(mensajeExito);
        
        // 3. Cerrar sesión y limpiar interfaz
        vista.cerrarSesion();
        vista.limpiarInterfaz();
        
        // 4. Reiniciar estado
        estadoActual = EstadoFinalizacion.INICIAL;
    }
    
    /**
     * Construye el texto del resumen de pago
     */
    private String construirResumenPago(String mensajeBeneficio, double costoFinal) {
        return "Resumen de Pago\n"
             + "---------------\n"
             + "Beneficio:  " + mensajeBeneficio + "\n"
             + "---------------\n"
             + "Total:      " + formatoMoneda(costoFinal);
    }
    
    /**
     * Formatea un valor monetario
     */
    private String formatoMoneda(double valor) {
        return String.format("$%,.2f", valor);
    }
    
    /**
     * Reinicia el proceso de finalización
     */
    public void reiniciarProceso() {
        estadoActual = EstadoFinalizacion.INICIAL;
        vista.reiniciarFlujo();
    }
    
    /**
     * Obtiene el estado actual del proceso
     */
    public EstadoFinalizacion getEstadoActual() {
        return estadoActual;
    }
    
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package obligatoriodda.pkg2025;

import Dominio.Cliente;
import Dominio.Comun;
import Dominio.DeLaCasa;
import Dominio.Dispositivo;
import Dominio.Frecuente;
import Dominio.Gestor;
import Dominio.TipoCliente;
import Servicios.Fachada;
import UI.VentanaLoginCliente;
import UI.VentanaPrincipal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class ObligatorioDDA2025 {

    public static void main(String[] args) {
        // Inicializar la Fachada y cargar datos de prueba
        Fachada fachada = Fachada.getInstancia();
        cargarDatosPrueba(fachada);

        SwingUtilities.invokeLater(() -> {
            // 1. Ventana principal del gestor
            new VentanaPrincipal().setVisible(true);

            // 2. Ventanas de login para cada dispositivo
            for (Dispositivo dispositivo : fachada.getDispositivos()) {
                new VentanaLoginCliente(dispositivo).setVisible(true); // Pasar el dispositivo asociado
            }
        });

    }

    private static void cargarDatosPrueba(Fachada fachada) {

        List<Dispositivo> dispositivos = new ArrayList<>();

        // Crear 5 dispositivos con IDs autoincrementales
        for (int i = 0; i < 5; i++) {
            Dispositivo d = new Dispositivo();
            Fachada.getInstancia().agregar(d);
        }
        
        TipoCliente comun = new Comun("comun");
        TipoCliente frecuente = new Frecuente("frecuente");
        TipoCliente deLaCasa = new DeLaCasa("deLaCasa");

        Cliente usuario1 = new Cliente("diego", "123", "444", comun);
        Cliente usuario2 = new Cliente("maria", "321", "555", frecuente);

        Gestor diegoAdmin = new Gestor("diego", "123", "Diego Gregoraz");
        Gestor admin1 = new Gestor("admin", "123", "Administrador General del Sistema");

        Fachada.getInstancia().agregar(diegoAdmin);
        Fachada.getInstancia().agregar(admin1);

        Fachada.getInstancia().agregar(usuario1);
        Fachada.getInstancia().agregar(usuario2);
    }
}

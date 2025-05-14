/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package obligatoriodda.pkg2025;

import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Gestor;
import Servicios.Fachada;
import UI.VentanaLoginCliente;
import UI.VentanaPrincipal;
import java.util.ArrayList;
import java.util.List;


public class ObligatorioDDA2025 {

    
    public static void main(String[] args) {
        
        Fachada f = Fachada.getInstancia();
        cargarDatosPrueba();
        new VentanaPrincipal().setVisible(true);
        
        for (Dispositivo d: f.getDispositivos()){
            new VentanaLoginCliente().setVisible(true);
        }
    }
    
    private static void cargarDatosPrueba() {
        
        List<Dispositivo> dispositivos = new ArrayList<>();

        // Crear 5 dispositivos con IDs autoincrementales
        for (int i = 0; i < 5; i++) {
            Dispositivo d = new Dispositivo();
            Fachada.getInstancia().agregar(d);
        }

      
        
        
        Cliente usuario1 = new Cliente("diego", "123", "444");
        Cliente usuario2 = new Cliente("maria", "321", "555");
        
        Gestor diegoAdmin = new Gestor("diego", "123", "Diego Gregoraz");
        Gestor admin1 = new Gestor("admin", "123", "Administrador General del Sistema");
        
        Fachada.getInstancia().agregar(diegoAdmin);
        Fachada.getInstancia().agregar(admin1);

      
        
       

        Fachada.getInstancia().agregar(usuario1); 
        Fachada.getInstancia().agregar(usuario2);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package obligatoriodda.pkg2025;

import Dominio.Cliente;
import Dominio.Gestor;
import Servicios.Fachada;
import UI.VentanaPrincipal;

/**
 *
 * @author usuar
 */
public class ObligatorioDDA2025 {

    
    public static void main(String[] args) {
        cargarDatosPrueba();
        new VentanaPrincipal().setVisible(true);
    }
    
    private static void cargarDatosPrueba() {
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package obligatoriodda.pkg2025;

import Dominio.Categoria;
import Dominio.Cliente;
import Dominio.Tipos.Comun;
import Dominio.Tipos.DeLaCasa;
import Dominio.Dispositivo;
import Dominio.Tipos.Frecuente;
import Dominio.Gestor;
import Dominio.Ingrediente;
import Dominio.Insumo;
import Dominio.Item;
import Dominio.Menu;
import Dominio.Tipos.TipoCliente;
import Dominio.UnidadProcesadora;
import Servicios.Fachada;
import Servicios.ServicioMenus;
import UI.ClienteUI;
import UI.VentanaLoginCliente;
import UI.VentanaPrincipal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class ObligatorioDDA2025 {

    public static void main(String[] args) {
        // Inicializar la Fachada y cargar datos de prueba
        Fachada f = Fachada.getInstancia();
        cargarDatosPrueba(f);

        // 1. Ventana principal del gestor
        new VentanaPrincipal().setVisible(true);

        // 2. Ventanas de login para cada dispositivo
        for (Dispositivo dispositivo : f.getDispositivos()) {
            new ClienteUI(dispositivo).setVisible(true); // Pasar el dispositivo asociado
        }

    }

    private static void cargarDatosPrueba(Fachada f) {

        // Crear dispositivos
        for (int i = 0; i < 5; i++) {
            Dispositivo d = new Dispositivo();
            f.agregar(d);
        }

        // Tipos de cliente
        TipoCliente comun = new Comun("comun");
        TipoCliente frecuente = new Frecuente("frecuente");
        TipoCliente deLaCasa = new DeLaCasa("deLaCasa");

        // Clientes
        Cliente usuario1 = new Cliente("diego", "123", "444", comun);
        Cliente usuario2 = new Cliente("maria", "321", "555", frecuente);
        f.agregar(usuario1);
        f.agregar(usuario2);

        // Gestores
        Gestor diegoAdmin = new Gestor("diego", "123", "Diego Gregoraz");
        Gestor admin1 = new Gestor("admin", "123", "Administrador General del Sistema");
        f.agregar(diegoAdmin);
        f.agregar(admin1);

        // Unidades procesadoras
        UnidadProcesadora cocina = new UnidadProcesadora("Cocina");
        UnidadProcesadora barra = new UnidadProcesadora("Barra");

        // Insumos
        Insumo pan = new Insumo("Pan", 100, 10);
        Insumo jamon = new Insumo("Jamón", 80, 5);
        Insumo queso = new Insumo("Queso", 60, 5);
        Insumo tomate = new Insumo("Tomate", 50, 5);

        // Ingredientes
        Ingrediente ingPan = new Ingrediente(pan, 2);
        Ingrediente ingJamon = new Ingrediente(jamon, 1);
        Ingrediente ingQueso = new Ingrediente(queso, 1);
        Ingrediente ingTomate = new Ingrediente(tomate, 1);

        // Items
        Item sandwich = new Item("Sándwich clásico", 150, cocina);
        sandwich.agregarIngrediente(ingPan);
        sandwich.agregarIngrediente(ingJamon);
        sandwich.agregarIngrediente(ingQueso);
        sandwich.agregarIngrediente(ingTomate);

        Item licuado = new Item("Licuado de frutas", 120, barra);

        // Categorías
        Categoria comida = new Categoria("Comidas");
        Categoria bebida = new Categoria("Bebidas");

        comida.agregarItem(sandwich);
        bebida.agregarItem(licuado);

        // Menú
        Menu menu = new Menu();
        menu.agregarCategoria(comida);
        menu.agregarCategoria(bebida);

        // Agregar menú al servicio correspondiente
        f.agregarMenu(menu);
        f.setMenuActivo(menu); // Asignamos menú activo
    }
}
    
    
    
    

/*
   SwingUtilities.invokeLater(() -> {
            // 1. Ventana principal del gestor
            new VentanaPrincipal().setVisible(true);

            // 2. Ventanas de login para cada dispositivo
            for (Dispositivo dispositivo : f.getDispositivos()) {
                new VentanaLoginCliente(dispositivo).setVisible(true); // Pasar el dispositivo asociado
            }
        });
         */

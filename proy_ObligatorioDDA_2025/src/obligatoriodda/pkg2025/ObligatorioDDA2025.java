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

        // Cliente
        Cliente usuario1 = new Cliente("Bruno", "123", "444", comun);
        Cliente usuario2 = new Cliente("Ricardo", "321", "555", frecuente);
        f.agregar(usuario1);
        f.agregar(usuario2);

        

        // Unidades procesadoras
        UnidadProcesadora cocina = new UnidadProcesadora("Cocina");
        UnidadProcesadora barra = new UnidadProcesadora("Barra");
        
        // Gestores
        Gestor diegoAdmin = new Gestor("diego", "123", "Diego Gregoraz", cocina);
        Gestor admin1 = new Gestor("admin", "123", "Administrador General del Sistema",barra);
        f.agregar(diegoAdmin);
        f.agregar(admin1);

        // ======================
        // 6. Insumos (ampliados para cafés y aguas)
        // ======================
        Insumo pan = new Insumo("Pan", 4, 2);
        Insumo jamon = new Insumo("Jamón", 2, 1);
        Insumo queso = new Insumo("Queso", 2, 1);
        Insumo tomate = new Insumo("Tomate", 2, 1);
        Insumo cafeGrano = new Insumo("Café en grano", 200, 100); // Nuevo insumo
        Insumo agua = new Insumo("Agua", 500, 300);              // Nuevo insumo

        // ======================
        // 7. Ingredientes (ampliados)
        // ======================
        Ingrediente ingPan = new Ingrediente(pan, 2);
        Ingrediente ingJamon = new Ingrediente(jamon, 1);
        Ingrediente ingQueso = new Ingrediente(queso, 1);
        Ingrediente ingTomate = new Ingrediente(tomate, 1);
        Ingrediente ingCafe = new Ingrediente(cafeGrano, 10); // Para café
        Ingrediente ingAgua = new Ingrediente(agua, 1);       // Para agua

        // ======================
        // 8. Categorías (nuevas: Cafés y Aguas)
        // ======================
        Categoria comida = new Categoria("Comidas");
        Categoria bebida = new Categoria("Bebidas");
        Categoria cafes = new Categoria("Cafés");    // Nueva categoría
        Categoria aguas = new Categoria("Aguas");    // Nueva categoría

        // ======================
        // 9. Ítems (corregidos con categorías)
        // ======================
        // ---- Ítems originales ----
        Item sandwich = new Item("Sándwich clásico", 150, cocina, comida);
        sandwich.agregarIngrediente(ingPan);
        sandwich.agregarIngrediente(ingJamon);
        sandwich.agregarIngrediente(ingQueso);
        sandwich.agregarIngrediente(ingTomate);

        Item licuado = new Item("Licuado de frutas", 120, barra, bebida);

        // ---- Nuevos ítems ----
        Item espresso = new Item("Espresso", 150, barra, cafes);
        espresso.agregarIngrediente(ingCafe);
        espresso.agregarIngrediente(ingAgua);

        Item aguaMineral = new Item("Agua Mineral", 80, barra, aguas);
        aguaMineral.agregarIngrediente(ingAgua);

        // ======================
        // 10. Asignar ítems a categorías
        // ======================
        comida.agregarItem(sandwich);
        bebida.agregarItem(licuado);
        cafes.agregarItem(espresso);
        aguas.agregarItem(aguaMineral);


        // 11. Menú (actualizado como singleton)
        // ======================
        Menu menu = Menu.getInstancia();  // Obtenemos la instancia singleton
        menu.agregarCategoria(comida);
        menu.agregarCategoria(bebida);
        menu.agregarCategoria(cafes);
        menu.agregarCategoria(aguas);

 
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

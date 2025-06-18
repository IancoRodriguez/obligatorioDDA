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
import Dominio.Tipos.Preferencial;
import Dominio.Tipos.TipoCliente;
import Dominio.UnidadProcesadora;
import Servicios.Fachada;

import UI.ClienteUI;
import UI.VentanaPrincipal;

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

        // ======================
        // 1. DISPOSITIVOS
        // ======================
        for (int i = 0; i < 8; i++) {
            Dispositivo d = new Dispositivo();
            f.agregar(d);
        }

        // ======================
        // 2. TIPOS DE CLIENTE
        // ======================
        TipoCliente comun = new Comun("comun");
        TipoCliente frecuente = new Frecuente("frecuente");
        TipoCliente deLaCasa = new DeLaCasa("deLaCasa");        
        TipoCliente preferencial = new Preferencial("preferencial");


        // ======================
        // 3. CLIENTES
        // ======================
        Cliente usuario1 = new Cliente("Bruno", "123", "444", comun);
        Cliente usuario2 = new Cliente("Ricardo", "123", "555", frecuente);
        Cliente usuario3 = new Cliente("María", "123", "666", deLaCasa);
        Cliente usuario4 = new Cliente("Carlos", "123", "777", comun);
        Cliente usuario5 = new Cliente("Ana", "123", "888", frecuente);
        Cliente usuario6 = new Cliente("Pedro", "123", "999", comun);
        Cliente usuario7 = new Cliente("Cacho", "123", "120", preferencial);

        f.agregar(usuario1);
        f.agregar(usuario2);
        f.agregar(usuario3);
        f.agregar(usuario4);
        f.agregar(usuario5);
        f.agregar(usuario6);       
        f.agregar(usuario7);


        // ======================
        // 4. UNIDADES PROCESADORAS
        // ======================
        UnidadProcesadora cocina = new UnidadProcesadora("Cocina");
        UnidadProcesadora barra = new UnidadProcesadora("Barra");
        UnidadProcesadora parrilla = new UnidadProcesadora("Parrilla");
        UnidadProcesadora heladeria = new UnidadProcesadora("Heladería");

        // ======================
        // 5. GESTORES
        // ======================
        Gestor diegoAdmin = new Gestor("juan", "123", "Juan Lopez", cocina);
        Gestor admin1 = new Gestor("admin", "123", "Administrador General", barra);
        Gestor parrillero = new Gestor("parrilla", "456", "Jefe de Parrilla", parrilla);
        Gestor heladero = new Gestor("helados", "789", "Maestro Heladero", heladeria);

        f.agregar(diegoAdmin);
        f.agregar(admin1);
        f.agregar(parrillero);
        f.agregar(heladero);

        // ======================
        // 6. INSUMOS COMPLETOS (stock calculado para cantidades específicas de ítems)
        // ======================
        // Panadería y bases
        Insumo pan = new Insumo("Pan", 4, 10);             // Solo para 2 sándwiches (2 panes c/u)
        Insumo panTostado = new Insumo("Pan Tostado", 15, 3);
        Insumo tortilla = new Insumo("Tortilla", 3, 8);    // Solo para 3 wraps

        // Carnes y proteínas
        Insumo jamon = new Insumo("Jamón", 2, 6);          // Solo para 2 sándwiches (1 jamón c/u)
        Insumo pollo = new Insumo("Pollo", 3, 8);          // Solo para 3 pollos grillados
        Insumo carne = new Insumo("Carne", 10, 5);          // Solo para 1 asado
        Insumo pescado = new Insumo("Pescado", 6, 1);

        // Lácteos
        Insumo queso = new Insumo("Queso", 5, 10);         // Solo para 5 sándwiches (1 queso c/u)
        Insumo leche = new Insumo("Leche", 6, 15);         // Solo para 3 licuados (2 leches c/u)
        Insumo crema = new Insumo("Crema", 3, 8);
        Insumo manteca = new Insumo("Manteca", 10, 2);

        // Vegetales
        Insumo tomate = new Insumo("Tomate", 8, 15);       // Solo para 4 sándwiches (2 tomates c/u)
        Insumo lechuga = new Insumo("Lechuga", 2, 8);      // Solo para 2 sándwiches completos
        Insumo cebolla = new Insumo("Cebolla", 20, 4);
        Insumo palta = new Insumo("Palta", 3, 8);          // Solo para 3 sándwiches completos

        // Frutas
        Insumo banana = new Insumo("Banana", 5, 12);       // Solo para 5 licuados de banana
        Insumo frutilla = new Insumo("Frutilla", 10, 20);  // Solo para 2 licuados de frutilla (5 c/u)
        Insumo durazno = new Insumo("Durazno", 15, 3);
        Insumo manzana = new Insumo("Manzana", 25, 6);

        // Bebidas base
        Insumo cafeGrano = new Insumo("Café en grano", 50, 100); // Solo para 5 cafés (10 granos c/u)
        Insumo agua = new Insumo("Agua", 15, 30);          // Solo para 15 bebidas/cafés
        Insumo te = new Insumo("Té", 4, 12);               // Solo para 4 tés
        Insumo chocolate = new Insumo("Chocolate", 15, 25);

        // Helados
        Insumo heladoVainilla = new Insumo("Helado Vainilla", 6, 15);  // Solo para 3 helados (2 porciones c/u)
        Insumo heladoChocolate = new Insumo("Helado Chocolate", 4, 12); // Solo para 2 helados (2 porciones c/u)
        Insumo heladoFrutilla = new Insumo("Helado Frutilla", 2, 10);   // Solo para 1 helado (2 porciones)

        // Condimentos y extras
        Insumo azucar = new Insumo("Azúcar", 8, 20);       // Solo para 8 cafés/licuados
        Insumo sal = new Insumo("Sal", 50, 10);
        Insumo aceite = new Insumo("Aceite", 4, 15);

        // ======================
        // 7. INGREDIENTES
        // ======================
        // Para sándwiches
        Ingrediente ingPan = new Ingrediente(pan, 2);
        Ingrediente ingJamon = new Ingrediente(jamon, 1);
        Ingrediente ingQueso = new Ingrediente(queso, 1);
        Ingrediente ingTomate = new Ingrediente(tomate, 2);
        Ingrediente ingLechuga = new Ingrediente(lechuga, 1);
        Ingrediente ingPalta = new Ingrediente(palta, 1);

        // Para cafés
        Ingrediente ingCafe = new Ingrediente(cafeGrano, 10);
        Ingrediente ingAguaCafe = new Ingrediente(agua, 1);
        Ingrediente ingLeche = new Ingrediente(leche, 1);
        Ingrediente ingAzucar = new Ingrediente(azucar, 1);

        // Para licuados
        Ingrediente ingBanana = new Ingrediente(banana, 1);
        Ingrediente ingFrutilla = new Ingrediente(frutilla, 5);
        Ingrediente ingLecheLicuado = new Ingrediente(leche, 2);
        Ingrediente ingAzucarLicuado = new Ingrediente(azucar, 1);

        // Para agua
        Ingrediente ingAgua = new Ingrediente(agua, 1);

        // Para helados
        Ingrediente ingHeladoVainilla = new Ingrediente(heladoVainilla, 2);
        Ingrediente ingHeladoChocolate = new Ingrediente(heladoChocolate, 2);
        Ingrediente ingHeladoFrutilla = new Ingrediente(heladoFrutilla, 2);

        // Para parrilla
        Ingrediente ingCarne = new Ingrediente(carne, 1);
        Ingrediente ingPollo = new Ingrediente(pollo, 1);
        Ingrediente ingSal = new Ingrediente(sal, 1);

        // Para tés
        Ingrediente ingTe = new Ingrediente(te, 1);
        Ingrediente ingAguaTe = new Ingrediente(agua, 1);

        // ======================
        // 8. CATEGORÍAS
        // ======================
        Categoria comidas = new Categoria("Comidas");
        Categoria bebidas = new Categoria("Bebidas");
        Categoria cafes = new Categoria("Cafés");
        Categoria aguas = new Categoria("Aguas");
        Categoria helados = new Categoria("Helados");
        Categoria parrilla_cat = new Categoria("Parrilla");
        Categoria tes = new Categoria("Tés");

        // ======================
        // 9. ÍTEMS COMPLETOS
        // ======================
        // ---- COMIDAS ----
        Item sandwich = new Item("Sándwich Clásico", 250, cocina, comidas);
        sandwich.agregarIngrediente(ingPan);
        sandwich.agregarIngrediente(ingJamon);
        sandwich.agregarIngrediente(ingQueso);
        sandwich.agregarIngrediente(ingTomate);

        Item sandwichCompleto = new Item("Sándwich Completo", 320, cocina, comidas);
        sandwichCompleto.agregarIngrediente(ingPan);
        sandwichCompleto.agregarIngrediente(ingJamon);
        sandwichCompleto.agregarIngrediente(ingQueso);
        sandwichCompleto.agregarIngrediente(ingTomate);
        sandwichCompleto.agregarIngrediente(ingLechuga);
        sandwichCompleto.agregarIngrediente(ingPalta);

        Item sandwichVegetariano = new Item("Sándwich Vegetariano", 200, cocina, comidas);
        sandwichVegetariano.agregarIngrediente(ingPan);
        sandwichVegetariano.agregarIngrediente(ingQueso);
        sandwichVegetariano.agregarIngrediente(ingTomate);
        sandwichVegetariano.agregarIngrediente(ingLechuga);
        sandwichVegetariano.agregarIngrediente(ingPalta);

        // ---- PARRILLA ----
        Item asado = new Item("Asado", 450, parrilla, parrilla_cat);
        asado.agregarIngrediente(ingCarne);
        asado.agregarIngrediente(ingSal);

        Item polloGrillado = new Item("Pollo Grillado", 380, parrilla, parrilla_cat);
        polloGrillado.agregarIngrediente(ingPollo);
        polloGrillado.agregarIngrediente(ingSal);

        // ---- CAFÉS ----
        Item espresso = new Item("Espresso", 150, barra, cafes);
        espresso.agregarIngrediente(ingCafe);
        espresso.agregarIngrediente(ingAguaCafe);

        Item cortado = new Item("Cortado", 180, barra, cafes);
        cortado.agregarIngrediente(ingCafe);
        cortado.agregarIngrediente(ingAguaCafe);
        cortado.agregarIngrediente(ingLeche);

        Item cafeConLeche = new Item("Café con Leche", 200, barra, cafes);
        cafeConLeche.agregarIngrediente(ingCafe);
        cafeConLeche.agregarIngrediente(ingAguaCafe);
        cafeConLeche.agregarIngrediente(ingLeche);
        cafeConLeche.agregarIngrediente(ingAzucar);

        Item cappuccino = new Item("Cappuccino", 220, barra, cafes);
        cappuccino.agregarIngrediente(ingCafe);
        cappuccino.agregarIngrediente(ingAguaCafe);
        cappuccino.agregarIngrediente(ingLeche);

        // ---- TÉS ----
        Item teComun = new Item("Té Común", 120, barra, tes);
        teComun.agregarIngrediente(ingTe);
        teComun.agregarIngrediente(ingAguaTe);

        Item teConLeche = new Item("Té con Leche", 150, barra, tes);
        teConLeche.agregarIngrediente(ingTe);
        teConLeche.agregarIngrediente(ingAguaTe);
        teConLeche.agregarIngrediente(ingLeche);

        // ---- BEBIDAS ----
        Item licuadoBanana = new Item("Licuado de Banana", 180, barra, bebidas);
        licuadoBanana.agregarIngrediente(ingBanana);
        licuadoBanana.agregarIngrediente(ingLecheLicuado);
        licuadoBanana.agregarIngrediente(ingAzucarLicuado);

        Item licuadoFrutilla = new Item("Licuado de Frutilla", 200, barra, bebidas);
        licuadoFrutilla.agregarIngrediente(ingFrutilla);
        licuadoFrutilla.agregarIngrediente(ingLecheLicuado);
        licuadoFrutilla.agregarIngrediente(ingAzucarLicuado);

        // ---- AGUAS ----
        Item aguaMineral = new Item("Agua Mineral", 80, barra, aguas);
        aguaMineral.agregarIngrediente(ingAgua);

        Item aguaSaborizada = new Item("Agua Saborizada", 100, barra, aguas);
        aguaSaborizada.agregarIngrediente(ingAgua);

        // ---- HELADOS ----
        Item heladoSimpleVainilla = new Item("Helado de Vainilla", 150, heladeria, helados);
        heladoSimpleVainilla.agregarIngrediente(ingHeladoVainilla);

        Item heladoSimpleChocolate = new Item("Helado de Chocolate", 150, heladeria, helados);
        heladoSimpleChocolate.agregarIngrediente(ingHeladoChocolate);

        Item heladoSimpleFrutilla = new Item("Helado de Frutilla", 150, heladeria, helados);
        heladoSimpleFrutilla.agregarIngrediente(ingHeladoFrutilla);

        Item heladoMixto = new Item("Helado Mixto", 220, heladeria, helados);
        heladoMixto.agregarIngrediente(ingHeladoVainilla);
        heladoMixto.agregarIngrediente(ingHeladoChocolate);
        heladoMixto.agregarIngrediente(ingHeladoFrutilla);

        // ======================
        // 10. ASIGNAR ÍTEMS A CATEGORÍAS
        // ======================
        // Comidas
        comidas.agregarItem(sandwich);
        comidas.agregarItem(sandwichCompleto);
        comidas.agregarItem(sandwichVegetariano);

        // Parrilla
        parrilla_cat.agregarItem(asado);
        parrilla_cat.agregarItem(polloGrillado);

        // Cafés
        cafes.agregarItem(espresso);
        cafes.agregarItem(cortado);
        cafes.agregarItem(cafeConLeche);
        cafes.agregarItem(cappuccino);

        // Tés
        tes.agregarItem(teComun);
        tes.agregarItem(teConLeche);

        // Bebidas
        bebidas.agregarItem(licuadoBanana);
        bebidas.agregarItem(licuadoFrutilla);

        // Aguas
        aguas.agregarItem(aguaMineral);
        aguas.agregarItem(aguaSaborizada);

        // Helados
        helados.agregarItem(heladoSimpleVainilla);
        helados.agregarItem(heladoSimpleChocolate);
        helados.agregarItem(heladoSimpleFrutilla);
        helados.agregarItem(heladoMixto);

        // ======================
        // 11. MENÚ SINGLETON
        // ======================
        Menu menu = Menu.getInstancia();
        menu.agregarCategoria(comidas);
        menu.agregarCategoria(parrilla_cat);
        menu.agregarCategoria(cafes);
        menu.agregarCategoria(tes);
        menu.agregarCategoria(bebidas);
        menu.agregarCategoria(aguas);
        menu.agregarCategoria(helados);
    }
}

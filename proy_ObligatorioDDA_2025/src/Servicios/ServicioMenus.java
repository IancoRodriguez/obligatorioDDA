/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios;

import Dominio.Categoria;
import Dominio.Item;
import Dominio.Menu;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class ServicioMenus {

    private List<Menu> menus;
    private Menu menuActivo;

    // Constructor sin parámetros, inicializa lista vacía
    public ServicioMenus() {
        this.menus = new ArrayList<>();
    }

    // Constructor que recibe lista de menús
    public ServicioMenus(List<Menu> menus) {
        this.menus = menus != null ? menus : new ArrayList<>();
        if (!this.menus.isEmpty()) {
            this.menuActivo = this.menus.get(0); // Por ejemplo, setea el primero como activo
        }
    }

    public void setMenuActivo(Menu menu) {
        this.menuActivo = menu;
    }

    public Menu getMenuActivo() {
        return menuActivo;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void agregarMenu(Menu menu) {
        menus.add(menu);
        if (menuActivo == null) {
            menuActivo = menu;
        }
    }

    public List<Categoria> obtenerCategorias() {
        if (menuActivo != null) {
            return menuActivo.getCategorias();
        }
        return new ArrayList<>();
    }

    public List<Item> obtenerItemsDeCategoria(String nombreCategoria) {
        if (menuActivo != null) {
            Categoria categoria = menuActivo.buscarCategoriaPorNombre(nombreCategoria);
            if (categoria != null) {
                return categoria.getItems();
            }
        }
        return new ArrayList<>();
    }

    public void agregarItem(String nombreCategoria, Item item) {
        Categoria categoria = buscarCategoriaPorNombre(nombreCategoria);
        if (categoria != null) {
            categoria.agregarItem(item);
        } else {
            System.out.println("No se encontró la categoría: " + nombreCategoria);
        }
    }

    private Categoria buscarCategoriaPorNombre(String nombreCategoria) {
        for (Categoria categoria : menuActivo.getCategorias()) {
            if (categoria.getNombre().equalsIgnoreCase(nombreCategoria)) {
                return categoria;
            }
        }
        return null;
    }
}

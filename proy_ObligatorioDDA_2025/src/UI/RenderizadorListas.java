/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import UI.Interfaces.ObtenerTexto;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author usuar
 */
public class RenderizadorListas<T> implements ListCellRenderer<T> {
    
    
    private final ObtenerTexto<T> obtenerTexto;

    // Constructor que recibe la implementación de ObtenerTexto
    public RenderizadorListas(ObtenerTexto<T> obtenerTexto) {
        this.obtenerTexto = obtenerTexto;
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends T> list,
            T value,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        JLabel label = new JLabel();
        label.setOpaque(true);

        // Personaliza el texto usando el valor de T
        label.setText(obtenerTexto.obtenerTexto(value));

        // Colores de selección
        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }

        return label;
    }

}

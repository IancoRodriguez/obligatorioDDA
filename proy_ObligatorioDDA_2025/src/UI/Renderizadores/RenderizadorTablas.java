/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI.Renderizadores;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RenderizadorTablas extends DefaultTableCellRenderer {

    private JLabel label;

    public RenderizadorTablas() {
        setLayout(new BorderLayout());
        label = new JLabel();
        add(label, BorderLayout.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (value != null) {
            label.setText(value.toString()); // Representación de texto del objeto
            if (value instanceof Icon) {
                label.setIcon((Icon) value);
                label.setText(""); // Eliminar texto si hay un icono
            }
        }
        return this;
    }

}

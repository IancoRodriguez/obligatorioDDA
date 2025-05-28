/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import UI.Renderizadores.RenderizadorListas;
import Dominio.Categoria;
import Dominio.Cliente;
import Dominio.Dispositivo;
import Dominio.Excepciones.*;
import Dominio.Item;
import Dominio.Menu;
import Dominio.Pedido;
import Dominio.Servicio;
import Dominio.Usuario;
import Servicios.Fachada;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ClienteUI extends javax.swing.JFrame {

    private Fachada f;
    private Dispositivo dispositivo;
    private Menu menu;
    private Servicio servicioActual;

    public ClienteUI(Dispositivo dispositivo) {
        initComponents();
        usuarioLogueadoFlag.setVisible(false);
        this.dispositivo = dispositivo;

        this.f = Fachada.getInstancia();
        this.menu = Menu.getInstancia();

        cargarCategorias();
        cargarItems();
        setVisible(true);

        // Listener para cargar la lista de items; 
        lCategorias.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarItems();
            }
        });

    }

    private void ingresar() throws UsuarioException, DispositivoException {
        String usuario = jUsuario.getText();
        String contrasena = new String(jContrasena.getPassword());

        try {
            
            if(!usuario.isBlank() && !contrasena.isBlank()){
                servicioActual = login(usuario, contrasena);          
                usuarioLogueadoFlag.setVisible(true);
            }else{
                cerrarSesion();
                throw new UsuarioException("Revise las credenciales ingresadas");
            }
            
            
        } catch(UsuarioException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login incorrecto", JOptionPane.ERROR_MESSAGE);
        }catch (DispositivoException e ){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login incorrecto", JOptionPane.ERROR_MESSAGE);
        }

    }

    public Servicio login(String usuario, String contrasena) throws UsuarioException, DispositivoException {
        return Fachada.getInstancia().loginCliente(usuario, contrasena, this.dispositivo);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jUsuario = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jBtnLogin = new javax.swing.JButton();
        jContrasena = new javax.swing.JPasswordField();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        btnEliminarPedido = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tComentario = new javax.swing.JTextArea();
        btnAgregarPedido = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lItems = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lCategorias = new javax.swing.JList<>();
        btnConfirmarPedidos = new javax.swing.JButton();
        btnFinalizarServicio = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaPedidos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        usuarioLogueadoFlag = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUsuarioActionPerformed(evt);
            }
        });

        jLabel1.setText("Numero cliente");

        jLabel2.setText("Contraseña");

        jBtnLogin.setText("Login");
        jBtnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnLoginActionPerformed(evt);
            }
        });

        btnEliminarPedido.setText("Eliminar pedido");
        btnEliminarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPedidoActionPerformed(evt);
            }
        });

        tComentario.setColumns(20);
        tComentario.setRows(5);
        jScrollPane3.setViewportView(tComentario);

        btnAgregarPedido.setText("Agregar pedido");
        btnAgregarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarPedidoActionPerformed(evt);
            }
        });

        jLabel5.setText("Comentario");

        jDesktopPane1.setLayer(btnEliminarPedido, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jScrollPane3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(btnAgregarPedido, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jDesktopPane1Layout.createSequentialGroup()
                        .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                                .addComponent(btnAgregarPedido)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarPedido)))
                        .addGap(0, 142, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregarPedido)
                    .addComponent(btnEliminarPedido))
                .addContainerGap())
        );

        jLabel3.setText("Items");

        jLabel4.setText("Categorias");

        jScrollPane1.setViewportView(lItems);

        jScrollPane2.setViewportView(lCategorias);

        jDesktopPane2.setLayer(jDesktopPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jDesktopPane2.setLayer(jScrollPane2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jDesktopPane2Layout = new javax.swing.GroupLayout(jDesktopPane2);
        jDesktopPane2.setLayout(jDesktopPane2Layout);
        jDesktopPane2Layout.setHorizontalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDesktopPane2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jDesktopPane1)))
                .addContainerGap())
        );
        jDesktopPane2Layout.setVerticalGroup(
            jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDesktopPane2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDesktopPane2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jDesktopPane2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jDesktopPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        btnConfirmarPedidos.setText("Confirmar Pedidos");
        btnConfirmarPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmarPedidosActionPerformed(evt);
            }
        });

        btnFinalizarServicio.setText("Finalizar Servicio");
        btnFinalizarServicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalizarServicioActionPerformed(evt);
            }
        });

        tablaPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Ítem", "Comentario", "Estado", "Unidad", "Gestor", "Precio"
            }
        ));
        jScrollPane5.setViewportView(tablaPedidos);

        jLabel6.setText("Pedidos del servicio");

        usuarioLogueadoFlag.setBackground(new java.awt.Color(51, 51, 51));
        usuarioLogueadoFlag.setForeground(new java.awt.Color(0, 102, 0));
        usuarioLogueadoFlag.setText("Usuario Logueado");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jDesktopPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(btnConfirmarPedidos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnFinalizarServicio))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jBtnLogin)
                                .addGap(27, 27, 27)
                                .addComponent(usuarioLogueadoFlag)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jScrollPane5)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jBtnLogin)
                    .addComponent(jContrasena, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(usuarioLogueadoFlag))
                .addGap(13, 13, 13)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDesktopPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnConfirmarPedidos)
                    .addComponent(btnFinalizarServicio))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(149, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jUsuarioActionPerformed

    private void jBtnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnLoginActionPerformed
        try {
            ingresar();
        } catch (UsuarioException ex) {
            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DispositivoException ex) {
            Logger.getLogger(ClienteUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jBtnLoginActionPerformed

    private void btnConfirmarPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarPedidosActionPerformed
        confimarPedidos();
    }//GEN-LAST:event_btnConfirmarPedidosActionPerformed

    private void btnFinalizarServicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalizarServicioActionPerformed
        finalizarServicio();
    }//GEN-LAST:event_btnFinalizarServicioActionPerformed

    private void btnAgregarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarPedidoActionPerformed
        registrarPedido();
    }//GEN-LAST:event_btnAgregarPedidoActionPerformed

    private void btnEliminarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPedidoActionPerformed
        eliminarPedido();
    }//GEN-LAST:event_btnEliminarPedidoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarPedido;
    private javax.swing.JButton btnConfirmarPedidos;
    private javax.swing.JButton btnEliminarPedido;
    private javax.swing.JButton btnFinalizarServicio;
    private javax.swing.JButton jBtnLogin;
    private javax.swing.JPasswordField jContrasena;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jUsuario;
    private javax.swing.JList<Categoria> lCategorias;
    private javax.swing.JList<Item> lItems;
    private javax.swing.JTextArea tComentario;
    private javax.swing.JTable tablaPedidos;
    private javax.swing.JLabel usuarioLogueadoFlag;
    // End of variables declaration//GEN-END:variables

    private void cargarCategorias() {
        try {
            // 1. Obtener datos desde la fachada
            DefaultListModel<Categoria> modelo = new DefaultListModel<>();
            for (Categoria cat : menu.getCategorias()) {
                modelo.addElement(cat);
            }

            // 2. Configurar modelo y renderizador
            lCategorias.setModel(modelo);
            lCategorias.setCellRenderer(new RenderizadorListas<>(
                    categoria -> categoria.getNombre()
            ));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar categorías: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }
    }

    private void cargarItems() {
        try {
            // Obtener items de la fachada 
            DefaultListModel<Item> modelo = new DefaultListModel<>();

            Categoria c = lCategorias.getSelectedValue();

            if (c != null) {
                for (Item item : c.getItems()) {
                    if (item.tieneStockDisponible()) {
                        modelo.addElement(item);
                    }
                }
            }

            //Configurar modelo y renderizador 
            lItems.setModel(modelo);
            lItems.setCellRenderer(new RenderizadorListas<>(
                    Item -> Item.getNombre()
            ));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar items: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }

    public void registrarPedido() {
        
        try {
            
            if(servicioActual == null){
                throw new ServicioException("Servicio no inicializado");
            }
            Item item = lItems.getSelectedValue();
            String comentario = tComentario.getText();
            Pedido nuevoPedido;

            nuevoPedido = new Pedido(item, comentario);
            
            servicioActual.agregarPedido(nuevoPedido);
            cargarPedidosEnTabla(servicioActual.getPedidos());
            

        } catch (SinStockException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Sin stock", JOptionPane.ERROR_MESSAGE);
        }catch (ServicioException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login incorrecto", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void cargarPedidosEnTabla(List<Pedido> pedidos) {
        // 1. Crear un modelo de tabla vacío
        DefaultTableModel modelo = new DefaultTableModel();

        // 2. Definir las columnas
        modelo.setColumnIdentifiers(new String[]{"Item", "Comentario", "Estado", "Unidad", "Gestor", "Precio"});

        // 3. Llenar el modelo con datos
        for (Pedido pedido : pedidos) {
            modelo.addRow(new Object[]{
                pedido.getItem().getNombre(), // Asegúrate de que Item tenga toString()
                pedido.getComentario(),
                pedido.getEstado(),
                pedido.getItem().getUnidadProcesadora(),
                pedido.getGestor(),
                pedido.getItem().getPrecioUnitario()
            });
        }

        // 4. Asignar el modelo a la tabla
        tablaPedidos.setModel(modelo);

        // 5. Ajustar el ancho de las columnas (opcional)
        tablaPedidos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 6. Actualizar la interfaz
        tablaPedidos.revalidate();
        tablaPedidos.repaint();
    }

    private void eliminarPedido() {
     
    try {
            
        if(servicioActual == null){
            throw new ServicioException("Servicio no inicializado");
        }

        Pedido quitarPedido = servicioActual.getPedidos().get(tablaPedidos.getSelectedRow());

        servicioActual.eliminarPedido(quitarPedido);
        cargarPedidosEnTabla(servicioActual.getPedidos());
            

        
        }catch (ServicioException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Login incorrecto", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void confimarPedidos() {
        
        try{
            servicioActual.confirmar();
            cargarPedidosEnTabla(servicioActual.getPedidos());
        }
        catch (SinStockException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Sin stock", JOptionPane.ERROR_MESSAGE);
        }       
        
    }
    
    private void finalizarServicio() {
        
        try{
            servicioActual.finalizar();
            cerrarSesion();
            
            cargarPedidosEnTabla(new ArrayList<Pedido>());
        }
        catch (ServicioException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "BOOM!", JOptionPane.ERROR_MESSAGE);
        }       
        
    }

    private void cerrarSesion() {
        try{
            usuarioLogueadoFlag.setVisible(false);
            jUsuario.setText("");
            jContrasena.setText("");
            
            servicioActual = null;
            dispositivo.setServicioActivo(servicioActual);
        }
        catch (DispositivoException ex){
            JOptionPane.showMessageDialog(this, ex.getMessage(), "BOOM!", JOptionPane.ERROR_MESSAGE);
        } 
    }

}

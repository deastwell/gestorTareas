package org.example.ui;

import org.example.domain.DBConnection;
import org.example.domain.tarea.Tarea;
import org.example.domain.tarea.TareaAdapter;
import org.example.domain.tarea.TareaDAOImp;
import org.example.domain.usuario.Usuario;
import org.example.domain.usuario.UsuarioDAOImp;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Ventana extends JFrame {
    public static final TareaDAOImp TAREA_DAO = new TareaDAOImp(DBConnection.getConnection());
    public static final UsuarioDAOImp DAO_USUARIOS = new UsuarioDAOImp(DBConnection.getConnection());

    private JPanel panel1;
    private JLabel info;
    private JTable table1;
    private JTextField txtTarea;
    private JTextField txtDescripcion;
    private JComboBox comboCategoria;
    private JComboBox comboPrioridad;
    private JComboBox comboUsuario;
    private JLabel txtId;
    private JButton actualizarButton;
    private JButton crearNuevoButton;
    private JButton borrarButton;

    private DefaultTableModel data;

    private ListSelectionModel selectionModel;

    private ArrayList<Tarea> tareas = new ArrayList<>(0);
    private ArrayList<Usuario> usuarios = new ArrayList<>(0);
    private Tarea tareaActual = null;

    public Ventana(){
        this.setContentPane(panel1);
        setSize(800,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Acceso con JDBC");

        comboPrioridad.addItem("Alta");
        comboPrioridad.addItem("Media");
        comboPrioridad.addItem("Baja");

        comboCategoria.addItem("Trabajo");
        comboCategoria.addItem("Personal");
        comboCategoria.addItem("Estudios");

        table1.setRowHeight(40);
        data = (DefaultTableModel) table1.getModel();
        data.addColumn("id");
        data.addColumn("tarea");
        data.addColumn("prioridad");
        data.addColumn("usuario");
        data.addColumn("categoria");
        data.addColumn("descripción");

        usuarios = DAO_USUARIOS.loadAll();
        comboUsuario.removeAllItems();
        // usuario 0 del combo es el 0 de la lista ...
        usuarios.forEach(usuario -> {
            comboUsuario.addItem(usuario.getNombre());
        });

        fillTable();

        table1.getSelectionModel().addListSelectionListener(evt -> showTareaDetails(evt) );
        actualizarButton.addActionListener(e -> updateTarea() );
        crearNuevoButton.addActionListener(e -> saveTarea() );
        borrarButton.addActionListener(e -> borrarTareaActual());
    }


    private void showTareaActualInPanel() {
        if (tareaActual != null) {
            txtId.setText(""+ tareaActual.getId() );
            txtDescripcion.setText( tareaActual.getDescripcion());
            txtTarea.setText(tareaActual.getTitulo());
            comboCategoria.setSelectedItem( tareaActual.getCategoria() );
            comboPrioridad.setSelectedItem( tareaActual.getPrioridad() );
            comboUsuario.setSelectedItem( tareaActual.getUsuario().getNombre() );
        } else {
            txtId.setText("");
            txtDescripcion.setText("");
            txtTarea.setText("");
            comboCategoria.setSelectedIndex(0);
            comboPrioridad.setSelectedIndex(0);
            comboUsuario.setSelectedIndex(0);
        }
    }

    private void updateTareaActualFromPanel() {
        tareaActual.setTitulo( txtTarea.getText());
        tareaActual.setUsuario_id( usuarios.get(comboUsuario.getSelectedIndex()).getId());
        tareaActual.setUsuario( usuarios.get(comboUsuario.getSelectedIndex()) );
        tareaActual.setPrioridad((String) comboPrioridad.getSelectedItem()) ;
        tareaActual.setCategoria((String) comboCategoria.getSelectedItem());
        tareaActual.setDescripcion( txtDescripcion.getText());
        System.out.println(tareaActual);
        info.setText(tareaActual.toString());
    }

    private void updateTarea() {
        if(tareaActual!=null) {
            updateTareaActualFromPanel();
            TAREA_DAO.update(tareaActual);

            fillTable();
        }
    }

    private void saveTarea() {
        tareaActual = new Tarea();
        updateTareaActualFromPanel();

        TAREA_DAO.save(tareaActual);

        showTareaActualInPanel();

        fillTable();
    }

    private void borrarTareaActual() {
        if(tareaActual!=null) {
            updateTareaActualFromPanel();
            TAREA_DAO.remove(tareaActual);
            tareaActual=null;

            fillTable();
        }
    }

    private void showTareaDetails(ListSelectionEvent evt) {
        if(!evt.getValueIsAdjusting()) {
            Integer selectedRow = table1.getSelectedRow();
            if(selectedRow>=0) {
                Tarea t = tareas.get(selectedRow);
                t = TAREA_DAO.load(t.getId());
                info.setText(t.toString());
                tareaActual = t;
                showTareaActualInPanel();
            }
        }
    }



    private void fillTable() {
        tareas = TAREA_DAO.loadAll();
        data.setRowCount(0);
        tareas.forEach( (t)->{
            //data.addRow( new TareaAdapter(t).toArrayString() ) ;
            data.addRow( new TareaAdapter(t).toArrayStringWithUser());
        });
        info.setText("Datos cargados correctamente");
    }

    public void load(){
        setVisible(true);
    }
}

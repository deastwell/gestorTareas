package org.example.domain.tarea;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TareaAdapter {

    private Tarea tarea = new Tarea();

    public TareaAdapter(){}

    public TareaAdapter(Tarea t){
        tarea=t;
    }

    public Tarea getTarea() {
        return tarea;
    }

    public void setTarea(Tarea tarea) {
        this.tarea = tarea;
    }

    public Tarea loadFromResultSet(ResultSet rs) throws SQLException {
        tarea.setId( rs.getLong("id") );
        tarea.setTitulo( rs.getString("titulo") );
        tarea.setPrioridad( rs.getString("prioridad") );
        tarea.setUsuario_id( rs.getLong("usuario_id") );
        tarea.setCategoria( rs.getString("categoria") );
        tarea.setDescripcion( rs.getString("descripcion") );
        return tarea;
    }

    public String[] toArrayString(){
        return new String[]{
                String.valueOf( tarea.getId()),
                tarea.getTitulo(),
                tarea.getPrioridad(),
                String.valueOf(tarea.getUsuario_id()),
                tarea.getCategoria(),
                tarea.getDescripcion()};
    }

    public String[] toArrayStringWithUser(){
        return new String[]{
                String.valueOf( tarea.getId()),
                tarea.getTitulo(),
                tarea.getPrioridad(),
                //new UsuarioDAOImp(DBConnection.getConnection()).load(tarea.getUsuario_id()).getNombre(),
                (tarea.getUsuario()!=null) ? tarea.getUsuario().getNombre() : "-",
                tarea.getCategoria(),
                tarea.getDescripcion()};
    }

}

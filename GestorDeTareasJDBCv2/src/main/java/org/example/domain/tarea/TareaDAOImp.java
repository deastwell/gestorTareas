package org.example.domain.tarea;

import lombok.extern.java.Log;
import org.example.domain.DBConnection;
import org.example.domain.usuario.UsuarioDAOImp;

import java.sql.*;
import java.util.ArrayList;
@Log
public class TareaDAOImp implements TareaDAO {

    private Connection connection;

    private final static String QUERY_LOAD_ALL = "SELECT * FROM tareas";
    private final static String QUERY_LOAD = "select * from tareas where id = ?";
    private final static String QUERY_UPDATE = "update tareas SET" +
            " titulo=?," +
            " prioridad=?," +
            " usuario_id=?," +
            " categoria=?," +
            " descripcion=? " +
            " WHERE id=?";
    private final static String QUERY_DELETE = "delete from tareas where id=?";
    private final static String QUERY_SAVE = "insert into tareas( titulo, prioridad, usuario_id, categoria, descripcion) " +
            "VALUES (?,?,?,?,?)";
    private final static String QUERY_LOAD_ALL_BY_RESPONSABLE = "SELECT * FROM tareas WHERE usuario_id=?";

    public TareaDAOImp(Connection c){
        connection = c;
    }

    @Override
    public Tarea load(Long id) {
        Tarea salida = null;

        try( var pst = connection.prepareStatement(QUERY_LOAD)){
            pst.setLong(1,id);
            var rs = pst.executeQuery();
            if(rs.next()){
                salida = (new TareaAdapter()).loadFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        salida.setUsuario( new UsuarioDAOImp(DBConnection.getConnection()).load(salida.getUsuario_id()));

        return salida;

    }

    @Override
    public ArrayList<Tarea> loadAll() {
        var salida = new ArrayList<Tarea>();

        try(Statement st= connection.createStatement()){
            ResultSet rs = st.executeQuery(QUERY_LOAD_ALL);

            while(rs.next()){
                Tarea t = (new TareaAdapter()).loadFromResultSet(rs);
                t.setUsuario( (new UsuarioDAOImp(connection)).load( t.getUsuario_id()) );
                salida.add( t );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public ArrayList<Tarea> loadAllByResponsable(Long responsable) {
        var salida = new ArrayList<Tarea>();

        try(PreparedStatement pst = connection.prepareStatement(QUERY_LOAD_ALL_BY_RESPONSABLE)){
            pst.setLong(1,responsable);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                salida.add( (new TareaAdapter()).loadFromResultSet(rs) );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public Tarea save(Tarea t) {
        try(PreparedStatement pst = connection.prepareStatement(QUERY_SAVE,Statement.RETURN_GENERATED_KEYS)){

            log.info(QUERY_SAVE);
            log.info(String.valueOf(t));
            pst.setString(1,t.getTitulo());
            pst.setString(2,t.getPrioridad());
            pst.setLong(3,t.getUsuario().getId());
            pst.setString(4,t.getCategoria());
            pst.setString(5,t.getDescripcion());

            if(pst.executeUpdate()==1){
                ResultSet ids = pst.getGeneratedKeys();
                ids.next();
                Long generatedId = ids.getLong(1);
                log.info("generatedId = "+generatedId);
                t.setId(generatedId);
            };
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return t;
    }

    @Override
    public Tarea update(Tarea t) {
        Tarea salida = null;

        if(t.getUsuario()!=null) {

            try (PreparedStatement pst = connection.prepareStatement(QUERY_UPDATE)) {
                log.info(QUERY_UPDATE);
                log.info(t.toString());
                pst.setString(1, t.getTitulo());
                pst.setString(2, t.getPrioridad());
                pst.setLong(3, t.getUsuario().getId());
                pst.setString(4,t.getCategoria());
                pst.setString(5,t.getDescripcion());
                pst.setLong(6,t.getId());

                int result = pst.executeUpdate();

                if(result == 1){
                    salida=t;
                    log.info("Tarea actualizada: "+salida.getId());
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

        return salida;
    }

    @Override
    public void remove(Tarea t) {
            try (PreparedStatement pst = connection.prepareStatement(QUERY_DELETE)) {
                log.info(QUERY_DELETE);
                log.info(t.toString());
                pst.setLong(1,t.getId());
                int result = pst.executeUpdate();

                if(result == 1){
                    log.info("Tarea eliminada");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

}

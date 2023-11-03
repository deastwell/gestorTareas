package org.example.domain.usuario;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UsuarioDAOImp implements UsuarioDAO {

    private final Connection connection;

    public UsuarioDAOImp(Connection c){
        connection = c;
    }

    private final static String queryLoadAll = "SELECT * FROM usuario";
    private final static String queryLoad = "select * from usuario where id = ?";
    private final static String queryLoadbyName = "select * from usuario where nombre = ?";

    @Override
    public Usuario load(Long id) {
        Usuario salida = null;

        try( var pst = connection.prepareStatement(queryLoad)){
            pst.setLong(1,id);
            var rs = pst.executeQuery();
            if(rs.next()){
                salida = new Usuario(rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    public Usuario load(String name) {
        Usuario salida = null;

        try( var pst = connection.prepareStatement(queryLoadbyName)){
            pst.setString(1,name);
            var rs = pst.executeQuery();
            if(rs.next()){
                salida = new Usuario(rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salida;
    }

    @Override
    public ArrayList<Usuario> loadAll() {
        var salida = new ArrayList<Usuario>();

        try(Statement st= connection.createStatement()){
            ResultSet rs = st.executeQuery(queryLoadAll);

            while(rs.next()){
                salida.add( new Usuario(rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getString("email")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return salida;
    }

    @Override
    public Usuario save(Usuario t) {
        return null;
    }

    @Override
    public Usuario update(Usuario t) {
        return null;
    }

    @Override
    public void remove(Usuario t) {

    }
}

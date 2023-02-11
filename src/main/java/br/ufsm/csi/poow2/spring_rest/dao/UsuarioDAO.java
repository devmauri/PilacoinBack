package br.ufsm.csi.poow2.spring_rest.dao;

import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.Cargo;
import br.ufsm.csi.poow2.spring_rest.model.Senha;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDAO {

    public ArrayList<Usuario> getUsuarios(){

        Statement stmt;
        ResultSet rs;
        String sql;

        ArrayList<Usuario> lista = new ArrayList<>();
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT usuario.id,usuario.nome, " +
                    "   senha.id AS senha_id , senha.senha, " +
                    " cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao as cargo_descricao " +
                    " FROM usuario,senha,cargo " +
                    " WHERE usuario.id=senha.fk_usuario_id " +
                    " AND usuario.fk_cargo_id = cargo.id;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                lista.add(parseRsToUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return lista;
    }


    public Usuario getUsuarioById(int id){

        PreparedStatement ptmt;
        ResultSet rs;
        String sql;

        Usuario usuario = null;
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT usuario.id, usuario.nome, " +
                    "   senha.id AS senha_id , senha.senha, " +
                    " cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao as cargo_descricao " +
                    " FROM usuario,senha,cargo " +
                    " WHERE usuario.id= ?  " +
                    " AND senha.fk_usuario_id = usuario.id " +
                    " AND usuario.fk_cargo_id = cargo.id;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,id);
            rs =ptmt.executeQuery();

            if (rs.next()){
                usuario = parseRsToUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return usuario;
    }

    public ArrayList<Usuario> getUsuarioByCargoId(int id){

        PreparedStatement ptmt;
        ResultSet rs;
        String sql;

        ArrayList<Usuario> lista = new ArrayList<>();
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT usuario.id, usuario.nome, " +
                    "   senha.id AS senha_id , senha.senha, " +
                    " cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao as cargo_descricao " +
                    " FROM usuario,senha,cargo " +
                    " WHERE usuario.fk_cargo_id = ? " +
                    " AND senha.fk_usuario_id = usuario.id " +
                    " AND usuario.fk_cargo_id = cargo.id;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,id);
            rs =ptmt.executeQuery();

            while (rs.next()){
                lista.add(parseRsToUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return lista;
    }

    public Usuario getUsuario(String nome){

        PreparedStatement ptmt;
        ResultSet rs;
        String sql;

        Usuario usuario = null;
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT usuario.id, usuario.nome, " +
                    "   senha.id AS senha_id , senha.senha, " +
                    " cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao as cargo_descricao " +
                    " FROM usuario,senha,cargo " +
                    " WHERE usuario.nome= ?  " +
                    " AND senha.fk_usuario_id = usuario.id " +
                    " AND usuario.fk_cargo_id = cargo.id;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1,nome);
            rs =ptmt.executeQuery();

            if (rs.next()){
                usuario = parseRsToUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return (usuario != null) ?
                usuario :
                new Usuario(-1, "", new Senha(), new Cargo());
    }


    public Usuario incluirUsuario(Usuario user){

        PreparedStatement ptmt;
        ResultSet rs;
        String sql;


        try(Connection conn = new ConectaDB().getConnection()){
            sql = "INSERT INTO usuario (fk_cargo_id, nome) " +
                    "values (?,?);";

            ptmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1,user.getCargo().getId());
            ptmt.setString(2,user.getNome());

            ptmt.execute();
            rs = ptmt.getGeneratedKeys();

            rs.next();
            user.setId(rs.getInt("id"));

            sql = "INSERT INTO senha (fk_usuario_id, senha) " +
                    "values (?,?);";
            ptmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1,user.getId());
            ptmt.setString(2,user.getSenha().getSenha());

            ptmt.execute();
            rs = ptmt.getGeneratedKeys();

            rs.next();
            user.getSenha().setId(rs.getInt("id"));

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }


    public boolean removerUsuario(Usuario user){
        PreparedStatement ptmt;
        String sql;

        try(Connection conn = new ConectaDB().getConnection()){

            if(user.getId() == null) return false;

            sql = "DELETE FROM senha WHERE fk_usuario_id=? ";
            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,user.getId());
            ptmt.execute();

            sql = "DELETE FROM usuario_atividade WHERE fk_usuario_id=? ";
            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,user.getId());
            ptmt.execute();

            sql = "DELETE FROM usuario WHERE id=? ";
            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,user.getId());
            ptmt.execute();


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    public static Usuario parseRsToUser(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario( "", new Cargo());

        String teste = "id";
        if(existe(rs,teste)){
            usuario.setId(rs.getInt(teste));
        }
        teste="nome";
        if(existe(rs,teste)){
            usuario.setNome(rs.getString(teste));
        }
        teste="senha";
        if(existe(rs,teste)){
            usuario.getSenha().setSenha(rs.getString(teste));
        }
        teste = "senha_id";
        if(existe(rs,teste)){
            usuario.getSenha().setId(rs.getInt(teste));
        }
        if(usuario.getId() !=null){
            usuario.getSenha().setUsuarioId(usuario.getId());
        }

        usuario.setCargo(CargoDAO.parseRsToCargo(rs));

        return usuario;
    }



    public static boolean existe(ResultSet rs, String coluna){
        try {
           return (rs.findColumn(coluna)>0) ? true : false;
        } catch (Exception e){
            return  false;
        }
    }

    public Usuario loginUser(String login){
        System.out.println("Login "+login);

        Usuario temp = this.getUsuario(login).copia();
        temp.getSenha().setSenha(new BCryptPasswordEncoder().encode(temp.getSenha().getSenha()));
        return temp;
    }

    public boolean editarUsuario(Usuario item){
        PreparedStatement ptmt;
        String sql;

        try(Connection conn = new ConectaDB().getConnection()){
            sql = "UPDATE public.usuario " +
                    " SET nome = ?, fk_cargo_id= ?" +
                    " WHERE usuario.id= ?;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1,item.getNome());
            ptmt.setInt(2, item.getCargo().getId());
            ptmt.setInt(3, item.getId());
            ptmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}

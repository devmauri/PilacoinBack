package br.ufsm.csi.poow2.spring_rest.dao;

import br.ufsm.csi.poow2.spring_rest.model.Atividade;

import java.sql.*;
import java.util.ArrayList;

public class AtividadeDAO {

    public static Atividade ParseRsToAtividade(ResultSet rs) throws SQLException{
        Atividade atividade = new Atividade();

        String teste = "atividade_id";
        if(UsuarioDAO.existe(rs,teste)){
            atividade.setId(rs.getInt(teste));
        }
        teste = "atividade_nome";
        if(UsuarioDAO.existe(rs,teste)){
            atividade.setNome(rs.getString(teste));
        }
        teste = "atividade_descricao";
        if(UsuarioDAO.existe(rs,teste)){
            atividade.setDescricao(rs.getString(teste));
        }

        atividade.setCargo(CargoDAO.parseRsToCargo(rs));

        return atividade;
    }

    public ArrayList<Atividade> getAtividades(){
        Statement stmt;
        ResultSet rs;
        String sql;

        ArrayList<Atividade> lista = new ArrayList<>();
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT atividade.id AS atividade_id ,atividade.nome as atividade_nome, atividade.descricao as atividade_descricao, " +
                    " cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao AS cargo_descricao " +
                    " FROM atividade, cargo " +
                    " WHERE atividade.fk_cargo_id = cargo.id;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                lista.add(ParseRsToAtividade(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return lista;
    }

    public Atividade getAtividadeById(int id){
        PreparedStatement ptmt;
        ResultSet rs;
        String sql;

        Atividade resp = null;
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT atividade.id AS atividade_id ,atividade.nome as atividade_nome, atividade.descricao as atividade_descricao, " +
                    " cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao AS cargo_descricao " +
                    " FROM atividade, cargo " +
                    " WHERE atividade.id= ?  " +
                    " AND atividade.fk_cargo_id = cargo.id;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,id);
            rs =ptmt.executeQuery();

            if (rs.next()){
                resp = ParseRsToAtividade(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return resp;
    }

    public boolean editarAtividade(Atividade item){
        PreparedStatement ptmt;
        String sql;

        try(Connection conn = new ConectaDB().getConnection()){
            sql = "UPDATE public.atividade " +
                    " SET nome = ?, descricao= ? , fk_cargo_id= ?" +
                    " WHERE atividade.id= ?;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1,item.getNome());
            ptmt.setString(2,item.getDescricao());
            ptmt.setInt(3, item.getCargo().getId());
            ptmt.setInt(4,item.getId());
            ptmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Atividade incluir(Atividade item){
        PreparedStatement ptmt;
        ResultSet rs;
        String sql;

        Atividade resp = null;
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "INSERT INTO atividade (fk_cargo_id, nome, descricao) " +
                    " values (?,?,?);";

            ptmt = conn.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            ptmt.setInt(1,item.getCargo().getId());
            ptmt.setString(2,item.getNome());
            ptmt.setString(3,item.getDescricao());

            ptmt.execute();
            rs = ptmt.getGeneratedKeys();

            if (rs.next()){
                resp = ParseRsToAtividade(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return resp;
    }
}

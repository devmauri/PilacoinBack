package br.ufsm.csi.poow2.spring_rest.dao;

import br.ufsm.csi.poow2.spring_rest.model.Cargo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CargoDAO {
    public ArrayList<Cargo> getCargos(){

        Statement stmt;
        ResultSet rs;
        String sql;


        ArrayList<Cargo> lista = new ArrayList<>();
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT cargo.id As cargo_id , cargo.nome AS cargo_nome, cargo.descricao AS cargo_descricao " +
                    " FROM cargo ;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                lista.add(parseRsToCargo(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return lista;
    }

    public static Cargo parseRsToCargo(ResultSet rs) throws SQLException {
        Cargo cargo = new Cargo();

        String teste = "cargo_nome";
        if(UsuarioDAO.existe(rs,teste)){
            cargo.setNome(rs.getString(teste));
        }
        teste="cargo_descricao";
        if(UsuarioDAO.existe(rs,teste)){
            cargo.setDescricao(rs.getString(teste));
        }
        teste="cargo_id";
        if(UsuarioDAO.existe(rs,teste)){
            cargo.setId(rs.getInt(teste));
        }

        return cargo;
    }
}

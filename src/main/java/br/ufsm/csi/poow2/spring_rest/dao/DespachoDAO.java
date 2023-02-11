package br.ufsm.csi.poow2.spring_rest.dao;

import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.Despacho;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DespachoDAO {

    public Boolean incluir(Despacho item){

        PreparedStatement ptmt;
        String sql;

        try(Connection conn = new ConectaDB().getConnection()){
            sql = "INSERT INTO usuario_atividade (fk_usuario_id, fk_atividade_id,data_despacho) " +
                    " values (?,?,?);";

            ptmt = conn.prepareStatement(sql);
            ptmt.setInt(1,item.getUsuario().getId());
            ptmt.setInt(2,item.getAtividade().getId());
            ptmt.setTimestamp(3,item.getDespacho());

            ptmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Despacho ParseRsToDespacho(ResultSet rs) throws SQLException{
        Despacho despacho = new Despacho();

        String teste = "despacho_id";
        if(UsuarioDAO.existe(rs,teste)){
            despacho.setId(rs.getInt(teste));
        }

        teste = "data_despacho";
        if(UsuarioDAO.existe(rs,teste)){
            despacho.setDespacho(rs.getTimestamp(teste));
        }

        teste = "data_inicio";
        if(UsuarioDAO.existe(rs,teste)){
            despacho.setInicio(rs.getTimestamp(teste));
        }

        teste = "data_termino";
        if(UsuarioDAO.existe(rs,teste)){
            despacho.setTermino(rs.getTimestamp(teste));
        }

        teste = "finalizado_ok";
        if(UsuarioDAO.existe(rs,teste)){
            despacho.setFinalizadoOk(rs.getBoolean(teste));
        }

        teste = "obs";
        if(UsuarioDAO.existe(rs,teste)){
            despacho.setObs(rs.getString(teste));
        }

        despacho.setUsuario(UsuarioDAO.parseRsToUser(rs));
        despacho.setAtividade(AtividadeDAO.ParseRsToAtividade(rs));

        despacho.setUsuario(new UsuarioDAO().getUsuarioById(despacho.getUsuario().getId()));
        despacho.setAtividade(new AtividadeDAO().getAtividadeById(despacho.getAtividade().getId()));

        return despacho;
    }

    public ArrayList<Despacho> getDespachos(){
        Statement stmt;
        ResultSet rs;
        String sql;

        ArrayList<Despacho> lista = new ArrayList<>();
        try(Connection conn = new ConectaDB().getConnection()){
            sql = "SELECT id AS despacho_id , fk_usuario_id as id, fk_atividade_id as atividade_id, " +
                    " data_despacho, data_inicio, data_termino, finalizado_ok, obs " +
                    " FROM usuario_atividade ;";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()){
                lista.add(ParseRsToDespacho(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return lista;
    }

    public boolean editarStatusDespacho(Despacho item){
        PreparedStatement ptmt;
        String sql;

        try(Connection conn = new ConectaDB().getConnection()){
            sql = "UPDATE public.usuario_atividade " +
                    " SET data_inicio=?, data_termino=?, finalizado_ok=?, obs=?" +
                    " WHERE id= ?;";

            ptmt = conn.prepareStatement(sql);
            ptmt.setTimestamp(1,item.getInicio());
            ptmt.setTimestamp(2,item.getTermino());
            ptmt.setBoolean(3, item.isFinalizadoOk());
            ptmt.setString(4,item.getObs());
            ptmt.setInt(5,item.getId());
            ptmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Boolean informarInicioDespacho(Despacho item){
        PreparedStatement ptmt;
        String sql;

        if(item.getId() <0) return false;

        if(item.getInicio()==null){
            item.setInicio(Timestamp.from(Instant.now()));
        }
        item.setTermino(null);
        item.setFinalizadoOk(false);
        item.setObs(null);
        return this.editarStatusDespacho(item);
    }

    public Boolean informarTerminoDespacho(Despacho item){
        PreparedStatement ptmt;
        String sql;

        if(item.getId() <0 || item.getInicio()==null) return false;

        if(item.getTermino()==null){
            item.setTermino(Timestamp.from(Instant.now()));
        }

        return this.editarStatusDespacho(item);
    }

    public int idUsuarioMes(){
        Despacho [] despachos = {};
        despachos = this.getDespachos().toArray(despachos);
        ArrayList<Elencar> ids = new ArrayList<Elencar>() ;

        for (int index=0; index < despachos.length; index++){
            if (despachos[index].getTermino()==null) continue;
            int procurado=despachos[index].getUsuario().getId();
            boolean achou=false;
            for(int indexb=0; indexb< ids.size();indexb++){
                if(ids.get(indexb).id==procurado){
                    ids.get(indexb).cout = ids.get(indexb).cout +1;
                    achou=true;
                    break;
                }
            }
            if(!achou){
                Elencar temp = new Elencar();
                temp.id = procurado;
                temp.cout =1;
                ids.add(temp);
            }
        }

        int id=0, countM=0;
        for(int index=0; index<ids.size(); index++){
            if (ids.get(index).cout>countM){
                countM = ids.get(index).cout;
                id = ids.get(index).id;
            }
        }

        return id;
    }


    private class Elencar{
        int id=0;
        int cout=0;
    }

}

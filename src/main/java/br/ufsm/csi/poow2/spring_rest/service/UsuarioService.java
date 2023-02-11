package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dao.AtividadeDAO;
import br.ufsm.csi.poow2.spring_rest.dao.UsuarioDAO;
import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;


import java.util.ArrayList;

public class UsuarioService {

    public RespostaDto<ArrayList> getUsuarios(){
        ArrayList<Usuario> list = new UsuarioDAO().getUsuarios();
        return  (list != null) ?
                    new RespostaDto<ArrayList>(RespostaDto.Status.SUCESSO,list):
                    new RespostaDto<ArrayList>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public RespostaDto<Usuario> getUsuarioById(int id){
        Usuario usuario = new UsuarioDAO().getUsuarioById(id);
        return  (usuario != null) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,usuario):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public RespostaDto<ArrayList> getUsuarioByCargoId(int id){
        ArrayList<Usuario> list = new UsuarioDAO().getUsuarioByCargoId(id);
        return  (list != null) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,list):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public RespostaDto<Usuario> getUsuario(String nome){
        Usuario usuario = new UsuarioDAO().getUsuario(nome);
        return  (usuario != null) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,usuario):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public RespostaDto<Usuario> incluirUsuario(Usuario user){
        Usuario temp = new UsuarioDAO().incluirUsuario(user);
        return (temp != null) ?
                new RespostaDto<Usuario>(RespostaDto.Status.SUCESSO,temp):
                new RespostaDto<Usuario>(RespostaDto.Status.ERRO, "Erro ao incluir Usuario.");
    }

    public RespostaDto<Boolean> removerUsuario(Usuario user){
        return (new UsuarioDAO().removerUsuario(user)) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,true):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro ao remover Usuario");
    }

    public RespostaDto<Boolean> editarUsuario (Usuario item){

        return  (new UsuarioDAO().editarUsuario(item)) ?
                new RespostaDto<Boolean>(RespostaDto.Status.SUCESSO,true):
                new RespostaDto<Boolean>(RespostaDto.Status.ERRO, "Erro não tratado ao editar Usuario no BD.",false);
    }

}

package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dao.AtividadeDAO;
import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;

import java.util.ArrayList;

public class AtividadeService {

    public RespostaDto<ArrayList> getAtividades(){
        ArrayList<Atividade> list = new AtividadeDAO().getAtividades();

        return (list !=null) ?
                new RespostaDto<ArrayList>(RespostaDto.Status.SUCESSO,list):
                new RespostaDto<ArrayList>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public RespostaDto<Atividade> getAtividadeById (int id){
        Atividade resp = new AtividadeDAO().getAtividadeById(id);

        return  (resp != null) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,resp):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public RespostaDto<Boolean> editarAtividade (Atividade item){

        return  (new AtividadeDAO().editarAtividade(item)) ?
                new RespostaDto<Boolean>(RespostaDto.Status.SUCESSO,true):
                new RespostaDto<Boolean>(RespostaDto.Status.ERRO, "Erro não tratado ao editar Atvidades no BD.",false);
    }

    public RespostaDto<Atividade> incluir (Atividade item){
        Atividade resp = new AtividadeDAO().incluir(item);

        return  (resp != null) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,resp):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao incluir no BD.");
    }

}

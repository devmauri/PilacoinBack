package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dao.AtividadeDAO;
import br.ufsm.csi.poow2.spring_rest.dao.DespachoDAO;
import br.ufsm.csi.poow2.spring_rest.dao.UsuarioDAO;
import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.Despacho;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;

import java.util.ArrayList;

public class DespachoService {

    public RespostaDto<Boolean> incluir(Despacho item){

        Usuario usuariotemp = new UsuarioDAO().getUsuarioById(item.getUsuario().getId());
        Atividade atividadetemp = new AtividadeDAO().getAtividadeById(item.getAtividade().getId());

        if(usuariotemp.getCargo().getId() != atividadetemp.getCargo().getId()){
            return new RespostaDto<>(RespostaDto.Status.ERRO,"Atividades devem ser despachadas somente para usuarios com cargos correspondentes.", false);
        }

        return (new DespachoDAO().incluir(item)) ?
                new RespostaDto<Boolean>(RespostaDto.Status.SUCESSO,true):
                new RespostaDto<Boolean>(RespostaDto.Status.ERRO, "Erro não tratado ao inlcuir despacho no BD.",false);
    }

    public RespostaDto<ArrayList> getDespachos (){
        ArrayList<Despacho> lista = new DespachoDAO().getDespachos();

        return (lista !=null) ?
                new RespostaDto<ArrayList>(RespostaDto.Status.SUCESSO,lista):
                new RespostaDto<ArrayList>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");

    }

    public RespostaDto<Boolean> informarInicioDespacho (Despacho item){

        return  (new DespachoDAO().informarInicioDespacho(item)) ?
                new RespostaDto<Boolean>(RespostaDto.Status.SUCESSO,true):
                new RespostaDto<Boolean>(RespostaDto.Status.ERRO, "Erro não tratado ao iniciar despacho no BD.",false);
    }

    public RespostaDto<Boolean> informarTerminoDespacho (Despacho item){

        return  (new DespachoDAO().informarTerminoDespacho(item)) ?
                new RespostaDto<Boolean>(RespostaDto.Status.SUCESSO,true):
                new RespostaDto<Boolean>(RespostaDto.Status.ERRO, "Erro não tratado ao terminar despacho no BD.",false);
    }

    public RespostaDto<Usuario> usuarioMes(){
        Usuario usuario = new UsuarioDAO().getUsuarioById(new DespachoDAO().idUsuarioMes());
        return  (usuario != null) ?
                new RespostaDto<>(RespostaDto.Status.SUCESSO,usuario):
                new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar usuario do mês no BD.");
    }
}

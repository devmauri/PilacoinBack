package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dao.CargoDAO;
import br.ufsm.csi.poow2.spring_rest.dao.UsuarioDAO;
import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Cargo;

import java.util.ArrayList;

public class CargoService {

    public RespostaDto<ArrayList> getCargos(){
        ArrayList<Cargo> list = new CargoDAO().getCargos();
        return  (list != null) ?
                new RespostaDto<ArrayList>(RespostaDto.Status.SUCESSO,list):
                new RespostaDto<ArrayList>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }
}

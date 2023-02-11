package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import br.ufsm.csi.poow2.spring_rest.repository.PilaCoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PilaCoinService {
    @Autowired
    private LogService log;
    @Autowired
    private  PilaCoinRepository pilaCoinRepository;

    public RespostaDto<ArrayList> jab;
    public void teste (){
        jab = pilaCoinsDto();
    }
    public void novoPila (PilaCoin novo){
        var tempL = new Log("ServidorB", "novoPila", Log.STATUS.OK,"");
        try{
            pilaCoinRepository.save(novo);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao criar novo pila no banco: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
        }
    }

    public List<PilaCoin>  pilaCoins(){
        var tempL = new Log("ServidorB", "Lista de pilaCoins", Log.STATUS.OK,"");
        List<PilaCoin> resp = new ArrayList<PilaCoin>();
        try{
            resp = pilaCoinRepository.findAll();
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Lista de pilaCoins: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }

    public RespostaDto<ArrayList> pilaCoinsDto(){

        var temp = new RespostaDto<ArrayList>();
        var arr = new ArrayList<PilaCoin>();
        temp.setStatus(RespostaDto.Status.SUCESSO);
        temp.setMsg("ok");
        //var rr = pilaCoins();
        var rr = CarregaBD.pilas;
        for (int i=0; i<rr.size(); i++){
            arr.add(rr.get(i));
        }
        temp.setDado(arr);

        return (arr !=null) ?
                    temp
                    : new RespostaDto<>(RespostaDto.Status.ERRO, "Erro não tratado ao consultar BD.");
    }

    public PilaCoin buscaPilaCoin(byte[] chave){
        var tempL = new Log("ServidorB", "buscaPilaCoin", Log.STATUS.OK,"");
        var resp = new PilaCoin();
        try{
            resp = pilaCoinRepository.pilaByKeyOwner(chave);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar PilaCoin especifico no banco de dados local: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }
}

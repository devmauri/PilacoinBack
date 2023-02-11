package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Transacao;
import br.ufsm.csi.poow2.spring_rest.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {
    @Autowired
    private LogService log;
    @Autowired
    private TransacaoRepository transacaoRepository;

    public void novaTransacao (Transacao novo){
        var tempL = new Log("ServidorB", "novaTransacao", Log.STATUS.OK,"");
        try{
            transacaoRepository.save(novo);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao fazer nova transação no banco: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
        }
    }

    public List<Transacao> Transacoes(){
        var tempL = new Log("ServidorB", "Lista de transações", Log.STATUS.OK,"");
        List<Transacao> resp = new ArrayList<>();
        try{
            resp = transacaoRepository.findAll();
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar transações: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }
}

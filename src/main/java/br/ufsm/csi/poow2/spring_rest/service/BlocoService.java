package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.model.entidades.Bloco;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.repository.BlocoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlocoService {
    @Autowired
    private LogService log;
    @Autowired
    private BlocoRepository blocoRepository;

    public void novoBloco (Bloco novo){
        var tempL = new Log("ServidorB", "novoBloco", Log.STATUS.OK,"");
        try{
            blocoRepository.save(novo);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao criar novo bloco no banco: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
        }
    }

    public List<Bloco> Blocos(){
        var tempL = new Log("ServidorB", "Lista de blocos", Log.STATUS.OK,"");
        List<Bloco> resp = new ArrayList<>();
        try{
            resp = blocoRepository.findAll();
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Lista de blocos: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }

    public Bloco buscaBloco(byte[] chave){
        var tempL = new Log("ServidorB", "buscaBloco", Log.STATUS.OK,"");
        var resp = new Bloco();
        try{
            resp = blocoRepository.blocoByKey(chave);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Bloco especifico no banco de dados local: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }
}

package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.BlocoDescobrir;
import br.ufsm.csi.poow2.spring_rest.model.PilaValidaEnvia;
import br.ufsm.csi.poow2.spring_rest.model.PilaValidaRest;
import br.ufsm.csi.poow2.spring_rest.model.TransacaoRest;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import br.ufsm.csi.poow2.spring_rest.model.events.RecebimentoWSC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

@Service
public class MineracaoService implements RecebimentoWSC.EventoRecepcao {
    @Value("${endereco.server}")
    private String enderecoServer;
    @Autowired
    private LogService logService;
    @Autowired
    private PilaService ps;

    public MineracaoService(){
        RecebimentoWSC.Singleton().addOuvinte(this);
    }
    @PostConstruct
    private void linkPilaService(){
        ps.informaMinerador(this);
    }


    private boolean minerando(){
        return ps.isMinerando();
    }

    public RespostaDto<String> iniciarMineracao(){
        var tempL = new Log("ServidorB", "iniciarMineracao", Log.STATUS.OK,"Iniciado mineracao");
        var resp = new RespostaDto<String>(RespostaDto.Status.SUCESSO, "","MINERANDO");
        try{
            if (!minerando()){
                resp.setDado(ps.startMineracao());
                if(ps.isMinerando()){
                    resp.setStatus(RespostaDto.Status.SUCESSO);
                }
                else{
                    resp.setStatus(RespostaDto.Status.ERRO);
                }
            }
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao iniciar mineração" + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            logService.registrar(tempL);
        }
        return resp;
    }

    public RespostaDto<String> pararMineracao(){
        var tempL = new Log("ServidorB", "pararMineracao", Log.STATUS.OK,"Parar mineracao");
        var resp = new RespostaDto<String>(RespostaDto.Status.SUCESSO, "","Mineração Parada");
        try{
            if (minerando()){
                resp.setDado(ps.pararMineracao());
                if(!ps.isMinerando()){
                    resp.setStatus(RespostaDto.Status.SUCESSO);
                }
                else{
                    resp.setStatus(RespostaDto.Status.ERRO);
                }
            }
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao parar mineração" + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            logService.registrar(tempL);
        }
        return resp;
    }

    public RespostaDto<String> statusMineracao(){
        var resp = new RespostaDto<String>(RespostaDto.Status.SUCESSO, "","Minerando");
        resp.setDado(ps.statusMineracao());
        return resp;
    }

    @Override
    public void recebidoWSC(RecebimentoWSC.EVENTOS acoes, Object... args) {
        try{
            switch (acoes) {
                case DIFICULDADE:
                    //dificuldade alterada pelo servidorA
                    if(args[0]==null) return;
                    ps.atualizaDificuldade((BigInteger) args[0]);
                    break;
                case PILA_ENVIADO:
                    //utilizar quando receber a confirmação de transferencia/validação dos pilas
                    break;
                case PILA_VALIDAR:
                    //Validar pila de outro usuario
                    ps.validarPilaOutroUsuario((PilaValidaRest) args[0]);
                    break;
                case BLOCO_MINERAR:
                    //minerar bloco do servidor
                    //ps.minerarBloco(args[0]);
                    var temp = (BlocoDescobrir[]) args;
                    for (var bloco :temp) {
                        ps.minerarBloco(bloco);
                    }
                    break;
                case BLOCO_VALIDAR:
                    //Validar bloco de outro usuario
                    ps.validaTransacao((TransacaoRest) args[0]);
                    break;
            }
        } catch (Exception e) {
            var tempL = new Log("ServidorB", "recebidoWSC",
                                Log.STATUS.NOK,"Erro durante processar recebimento de websockt ->");
            tempL.addObs(e.getMessage());
            logService.registrar(tempL);
        }

    }

    public void confirmouPilaOutroUsuario(PilaValidaEnvia pila){
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PilaValidaEnvia> entity = new HttpEntity<>(pila, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<PilaValidaEnvia> resp = restTemplate.postForEntity("http://" + enderecoServer + "/pilacoin/validaPilaOutroUsuario/", entity, PilaValidaEnvia.class);

        } catch (Exception e) {
            var tempL = new Log("ServidorB", "confirmouPilaOutroUsuario", Log.STATUS.NOK,"Erro ao enviar pila validado de outro usuario: ");
            tempL.addObs(e.getMessage());
            logService.registrar(tempL);
        }
    }

    public PilaCoin enviarPilaCoinParaValidacao(PilaCoin pila) {
        PilaCoin resp = null;
        var tempL = new Log();
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<PilaCoin> entity = new HttpEntity<>(pila, headers);
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<PilaCoin> response = restTemplate.postForEntity("http://" + enderecoServer + "/pilacoin/", entity, PilaCoin.class);
            resp = response.getBody();
        } catch (Exception e) {
            tempL = new Log("ServidorB", "enviarPilaCoinParaValidacao", Log.STATUS.NOK,"Erro ao enviar pila para validação no servidor A: ");
            tempL.addObs(e.getMessage());
        }finally {
            //logService.registrar(tempL);
        }
        return resp;
    }

}

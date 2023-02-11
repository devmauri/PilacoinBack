package br.ufsm.csi.poow2.spring_rest.service.Handler;

import br.ufsm.csi.poow2.spring_rest.model.BlocoDescobrir;
import br.ufsm.csi.poow2.spring_rest.model.TransacaoDescobrir;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.events.RecebimentoWSC;
import br.ufsm.csi.poow2.spring_rest.model.DificuldadeRet;
import br.ufsm.csi.poow2.spring_rest.model.PilaValidaRest;
import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import br.ufsm.csi.poow2.spring_rest.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.Base64;


public class MyStompSessionHandler implements StompSessionHandler {

    private RecebimentoWSC acaoRecebimento = RecebimentoWSC.Singleton();
    private LogService log;

    public MyStompSessionHandler(){}

    public MyStompSessionHandler(LogService log){
        this();
        this.log = log;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        var tempL = new Log("ServidorB", "afterConnected", Log.STATUS.OK,"Inscreve nos tópicos do SERVIDOR-A");
        try{
            stompSession.subscribe("/topic/dificuldade", this);
            stompSession.subscribe("/topic/validaMineracao", this);
            stompSession.subscribe("/topic/descobrirNovoBloco", this);
            stompSession.subscribe("/topic/validaBloco", this);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao se inscrever nos tópicos do SERVIDOR-A: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
        }

    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {
        var tempL = new Log("ServidorB", "handleException", Log.STATUS.NOK,"Exceção vindo do SERVIDOR-A");
        try{
            tempL.addObs("handleException " + stompHeaders.getDestination());
            tempL.addObs("bytes " + Base64.getEncoder().encodeToString(bytes));
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao tratar exceção vinda do SERVIDOR-A: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
        }
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {
        var tempL = new Log("ServidorB", "handleTransportError", Log.STATUS.NOK,"Dados enviados errados para SERVIDOR-A");
        try{
            tempL.addObs("handleTransportError " + stompSession.toString());
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao tratar exceção após dados enviados errados ao SERVIDOR-A: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            //log.registrar(tempL);
        }
    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        if(stompHeaders.getDestination() == null){
            var tempL = new Log("ServidorB", "getPayloadType", Log.STATUS.NOK,"stompHeaders vindo do Servidor-A esta nulo.");
            log.registrar(tempL);
            return null;
        }

        switch (stompHeaders.getDestination()){
            case "/topic/dificuldade":
                return DificuldadeRet.class;
            case "/topic/validaMineracao":
                return PilaValidaRest.class;
            case "/topic/descobrirNovoBloco":
                return BlocoDescobrir.class;
            case "/topic/validaBloco":
                return TransacaoDescobrir.class;
            default:
                var tempL = new Log("ServidorB", "getPayloadType",
                                    Log.STATUS.NOK,"Falta especificar a classe para o destino de: "+stompHeaders.getDestination());
                break;
        }
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        if(payload == null) {
            var tempL = new Log("ServidorB", "handleFrame",
                                Log.STATUS.NOK,"payload nulo vindo do Servidor-A.");
            log.registrar(tempL);
            return;
        }

        if(payload.getClass().equals(DificuldadeRet.class)){
            var temp = new BigInteger(((DificuldadeRet) payload).getDificuldade(), 16);
            acaoRecebimento.invokeAcoes(RecebimentoWSC.EVENTOS.DIFICULDADE, temp);
        }
        else if(payload.getClass().equals(PilaValidaRest.class)){
            var temp = ((PilaValidaRest) payload);
            if(temp !=null){
                acaoRecebimento.invokeAcoes(RecebimentoWSC.EVENTOS.PILA_VALIDAR,temp);
            }
        }
        else if(payload.getClass().equals(BlocoDescobrir.class)){
            var temp = ((BlocoDescobrir) payload);
            if(temp !=null){
                acaoRecebimento.invokeAcoes(RecebimentoWSC.EVENTOS.BLOCO_MINERAR,temp);
            }
        }
        else if(payload.getClass().equals(TransacaoDescobrir.class)){
            var temp = ((TransacaoDescobrir) payload);
            if(temp !=null){
                acaoRecebimento.invokeAcoes(RecebimentoWSC.EVENTOS.BLOCO_VALIDAR,temp);
            }
        }
        else{
            var tempL = new Log("ServidorB", "handleFrame",
                    Log.STATUS.NOK,"payload vindo do Servidor-A não reflete nenhuma classe." + payload);
            log.registrar(tempL);
        }
    }
}

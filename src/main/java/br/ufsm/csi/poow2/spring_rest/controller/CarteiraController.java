package br.ufsm.csi.poow2.spring_rest.controller;

import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import br.ufsm.csi.poow2.spring_rest.service.*;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.net.ssl.SSLEngineResult;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/carteira")
public class CarteiraController {
    @Autowired
    private PilaCoinService pilaCoinService;
    @Autowired
    private LogService logService;
    @Autowired
    private MineracaoService mineracaoService;


    @GetMapping("/listar")
    public RespostaDto<ArrayList> getCarteira() {
        return pilaCoinService.pilaCoinsDto();
    }

    @GetMapping("/pila")
    public PilaCoin getPila(@RequestParam(value="id") int id) {

        var pila = new PilaCoin();
        pila.setChaveCriador("chave1".getBytes(StandardCharsets.UTF_8));
        pila.setNonce("nonce9");
        pila.setStatus("mock");
        pila.setAssinaturaMaster("mster assinaura".getBytes(StandardCharsets.UTF_8));
        pila.setIdCriador("idcriador");
        pila.setDataCriacao(Time.from(Instant.now()));
        return pila;
    }

    @GetMapping("/eventos")
    public RespostaDto<ArrayList> getEventos() {
        var temp = (ArrayList) logService.allLogs();

        return (temp != null)
                ? new RespostaDto<ArrayList>(RespostaDto.Status.SUCESSO,temp)
                : new RespostaDto<ArrayList> (RespostaDto.Status.ERRO, "Erro, getEventos ");
    }

    @PostMapping("/stop")
    public RespostaDto<String> pararMineracao(
            @RequestBody String acao){
        return mineracaoService.pararMineracao();
    }

    @PostMapping("/start")
    public RespostaDto<String> comecarMineracao(
            @RequestBody String acao){
        return mineracaoService.iniciarMineracao();
    }

    @GetMapping("/statusMineracao")
    public RespostaDto<String> statusMineracao(){ return mineracaoService.statusMineracao();}
}

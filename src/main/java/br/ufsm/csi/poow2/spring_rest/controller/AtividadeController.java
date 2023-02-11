package br.ufsm.csi.poow2.spring_rest.controller;

import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.service.AtividadeService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/atividades")
public class AtividadeController {

    @GetMapping("/listar")
    public RespostaDto<ArrayList> getAtividades() { return new AtividadeService().getAtividades();}

    @GetMapping("/getAtividadeById")
    public RespostaDto<Atividade> getAtividadeById(
            @RequestParam(value="id") int id){
        return new AtividadeService().getAtividadeById(id);
    }

    @PutMapping("/editar")
    public RespostaDto<Boolean> editarAtividade(
            @RequestBody Atividade item){
        return new AtividadeService().editarAtividade(item);
    }

    @PostMapping("/novo")
    public RespostaDto<Atividade> incluir(
            @RequestBody Atividade item){
        return new AtividadeService().incluir(item);
    }
}

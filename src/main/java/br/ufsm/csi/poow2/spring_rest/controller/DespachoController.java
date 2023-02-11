package br.ufsm.csi.poow2.spring_rest.controller;

import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.Despacho;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;
import br.ufsm.csi.poow2.spring_rest.service.AtividadeService;
import br.ufsm.csi.poow2.spring_rest.service.DespachoService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/despachos")
public class DespachoController {

    @PostMapping("/novo")
    public RespostaDto<Boolean> incluir(
            @RequestBody Despacho item
            ){
        return new DespachoService().incluir(item);
    }

    @GetMapping("/listar")
    public RespostaDto<ArrayList> getDespachos(){
        return new DespachoService().getDespachos();
    }

    @PutMapping("/iniciar")
    public RespostaDto<Boolean> informarInicioDespacho(
            @RequestBody Despacho item){
        return new DespachoService().informarInicioDespacho(item);
    }

    @PutMapping("/terminar")
    public RespostaDto<Boolean> informarTerminoDespacho(
            @RequestBody Despacho item){
        return new DespachoService().informarTerminoDespacho(item);
    }

    @GetMapping("/usuarioMes")
    public RespostaDto<Usuario> usuarioMes(){
        return new DespachoService().usuarioMes();
    }
}

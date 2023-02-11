package br.ufsm.csi.poow2.spring_rest.controller;

import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.service.CargoService;
import br.ufsm.csi.poow2.spring_rest.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    @GetMapping("/listar")
    public RespostaDto<ArrayList> getCargos(){
        return new CargoService().getCargos();
    }

}

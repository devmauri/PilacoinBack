package br.ufsm.csi.poow2.spring_rest.controller;


import br.ufsm.csi.poow2.spring_rest.dto.RespostaDto;
import br.ufsm.csi.poow2.spring_rest.model.Atividade;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;
import br.ufsm.csi.poow2.spring_rest.service.AtividadeService;
import br.ufsm.csi.poow2.spring_rest.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @GetMapping("/listar")
    public RespostaDto<ArrayList> getUsuarios(){
        return new UsuarioService().getUsuarios();
    }

    @GetMapping("/getUsuarioById")
    public RespostaDto<Usuario> getUsuarioById(
            @RequestParam(value="id") int id){
        return new UsuarioService().getUsuarioById(id);
    }

    @GetMapping("/getUsuarioByCargoId")
    public RespostaDto<ArrayList> getUsuarioByCargoId(
            @RequestParam(value="id") int id){
        return new UsuarioService().getUsuarioByCargoId(id);
    }

    @GetMapping("/getUsuario")
    public RespostaDto<Usuario> getUsuario(
            @RequestParam(value="login") String login){
        return new UsuarioService().getUsuario(login);
    }

    @PostMapping("/incluir")
    public RespostaDto<Usuario> incluirUsuario(
            @RequestBody Usuario user){
        return new UsuarioService().incluirUsuario(user);
    }

    @DeleteMapping("/excluir")
    public RespostaDto<Boolean> removerUsuario(
            @RequestBody Usuario user){
        return new UsuarioService().removerUsuario(user);
    }

    @PutMapping("/editar")
    public RespostaDto<Boolean> editarUsuario(
            @RequestBody Usuario item){
        return new UsuarioService().editarUsuario(item);
    }

}

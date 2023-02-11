package br.ufsm.csi.poow2.spring_rest.controller;

import br.ufsm.csi.poow2.spring_rest.dao.ClienteDAO;
import br.ufsm.csi.poow2.spring_rest.model.Cliente;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @GetMapping ("/cliente")
    public Cliente getCliente(){
        return new ClienteDAO().getCliente(3);
    }

    @GetMapping("/clientes")
    public ArrayList<Cliente> getClientes(){
        return new ClienteDAO().getClientes();
    }

    @GetMapping("/ping")
    public String ping(){
        return "Modulo ClienteController funcionando.";
    }

    @PostMapping("cadastrar")
    public String cadastrarCliente(@RequestParam(value="id") int id,
                                   @RequestParam(value="nome") String nome) {

        return (new ClienteDAO().incluir(new Cliente(id,nome)) != null) ? "Cadastrado " : "Falha no cadastro";
    }

    @PostMapping("incluir")
    public Cliente incluirCliente(@RequestBody Cliente c) {

        return new ClienteDAO().incluir(c);
    }

}

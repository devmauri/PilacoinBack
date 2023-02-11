package br.ufsm.csi.poow2.spring_rest.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {

    @CrossOrigin(origins = "*")
    @GetMapping("/ping")
    public String ping(){
        return " API rodando - Retorno de TesteController.ping()";
    }

    @GetMapping("/gamebox/ficha.php")
    public String pong(@RequestParam (value = "id") int id,
                       @RequestParam (value = "inseridas") int inseridas,
                       @RequestParam (value = "total") int total,
                       @RequestParam (value = "tempo") int tempo,
                       @RequestParam (value = "sinal") int sinal,
                       @RequestParam (value = "erroconexao") int erroConexao,
                       @RequestParam (value = "travada") boolean travada){

        if(tempo==0){
            System.out.println("Maquina ligada: \n\t id: " +
                    "\n\t Inseridas: " + inseridas+
                    "\n\t Total Fichas: "+total+
                    "\n\t Horas Ligada: "+ tempo/60 +
                    "\n\t Sinal: " + sinal+
                    "\n\t Erros na Conexão: " + erroConexao +
                    "\n\t Ficha Travada: " + travada);
        }
        else if (inseridas>0){
            System.out.println("Ficha inserida: \n\t id: " + id +
                    "\n\t Inseridas: " + inseridas+
                    "\n\t Total Fichas: "+total+
                    "\n\t Horas Ligada: "+ tempo/60 +
                    "\n\t Sinal: " + sinal+
                    "\n\t Erros na Conexão: " + erroConexao +
                    "\n\t Ficha Travada: " + travada);
        }
        else if(travada){
            System.out.println("Ficha Travada: \n\t id: " + id +
                    "\n\t Inseridas: " + inseridas+
                    "\n\t Total Fichas: "+total+
                    "\n\t Horas Ligada: "+ tempo/60 +
                    "\n\t Sinal: " + sinal+
                    "\n\t Erros na Conexão: " + erroConexao +
                    "\n\t Ficha Travada: " + travada);
        }
        else{
            System.out.println("Pong Maquina: \n\t id: " + id +
                    "\n\t Inseridas: " + inseridas+
                    "\n\t Total Fichas: "+total+
                    "\n\t Horas Ligada: "+ tempo/60 +
                    "\n\t Sinal: " + sinal+
                    "\n\t Erros na Conexão: " + erroConexao +
                    "\n\t Ficha Travada: " + travada);
        }
        return "Dados atualizados";
    }

}

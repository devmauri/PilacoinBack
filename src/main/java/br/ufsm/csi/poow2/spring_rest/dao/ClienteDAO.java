package br.ufsm.csi.poow2.spring_rest.dao;

import br.ufsm.csi.poow2.spring_rest.model.Cliente;

import java.util.ArrayList;

//INSERT INTO cargo (id, nome, descricao) values (2, 'Caixa', 'Apresenta NF, recebe pagamento');
//INSERT INTO Atividade (id, fk_cargo_id, nome, descricao) values (0, 1, 'Limpar Mesa', 'Limpar a mesa para clientes utilizarem');
//INSERT INTO usuario (id, fk_cargo_id, nome) values (0, 1, 'Paulo');
//INSERT INTO senha (id, fk_usuario_id, senha) values (0, 1, 'senha');

public class ClienteDAO {

    public Cliente incluir(Cliente c){
        System.out.println("Abrir conexão BD - incluir cliente");
        System.out.println("Executar SQL - incluir cliente");
        System.out.println("Salvar o cliente: "+ c.getNome());

        return c;
    }

    public boolean deletar(Cliente c){
        System.out.println("Abrir conexão BD - deletar cliente");
        System.out.println("Executar SQL - deletar cliente");
        System.out.println("Deletar o cliente: "+ c.getNome());

        return true;
    }

    public ArrayList <Cliente> getClientes (){
        ArrayList<Cliente> cientes = new ArrayList<>();

        System.out.println("Abrir conexão BD - Pegar cliente cliente");
        System.out.println("Executar SQL - pegar cliente");
        System.out.println("Retorna uma lista de cliente: ");

        cientes.add(new Cliente(1,"Maria"));
        cientes.add(new Cliente(2,"Carlota"));
        cientes.add(new Cliente(3,"Joaquina"));

        return cientes;
    }

    public Cliente getCliente(int id){
        System.out.println("Abrir conexão BD - getCliente");
        System.out.println("Executar SQL - getCliente");
        System.out.println("Retorna cliente com id: "+ id);

        return new Cliente(3,"Joaquina");
    }

}

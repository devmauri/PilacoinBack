package br.ufsm.csi.poow2.spring_rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioRest {
    private long id;
    private String nome;
    private byte[] chavePublica;

    public UsuarioRest(){

    }

    public UsuarioRest(String nome){
        this.nome = nome;
    }

    public UsuarioRest(byte[] kp){
        this.chavePublica = kp;
    }

    public UsuarioRest(String nome, byte[] kp){
        this.nome = nome;
        this.chavePublica = kp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public byte[] getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(byte[] chavePublica) {
        this.chavePublica = chavePublica;
    }
}

package br.ufsm.csi.poow2.spring_rest.model;

public class Atividade {
    private int id;
    private String nome;
    private String descricao;
    private Cargo cargo;

    public Atividade (){}

    public Atividade(int id, String nome, String descricao, Cargo cargo) {
        this.id = id;
        this.cargo = cargo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cargo getCargo() { return cargo;}

    public void setCargo(Cargo cargo) {this.cargo = cargo;}
}

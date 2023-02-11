package br.ufsm.csi.poow2.spring_rest.model;

public class Senha {

    private int id;
    private int usuarioId;
    private String senha;

    public Senha(){}

    public Senha(int id, int usuarioId, String senha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getUsuarioId() {return usuarioId; }

    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public Senha copia(){
        return new Senha(this.id,this.usuarioId,this.senha);
    }
}

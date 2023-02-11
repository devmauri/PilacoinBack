package br.ufsm.csi.poow2.spring_rest.model;

import java.sql.Timestamp;
import java.time.Instant;


public class Despacho {
    private int id;
    private Usuario usuario;
    private Atividade atividade;
    private Timestamp despachado;
    private Timestamp inicio;
    private Timestamp termino;
    private boolean finalizadoOk;
    private String obs;

    public Despacho() {
        this.despachado = Timestamp.from(Instant.now());
        this.obs="";
    }

    public Despacho(int id, Usuario usuario, Atividade atividade, Timestamp despacho) {
        this();
        this.id = id;
        this.usuario = usuario;
        this.atividade = atividade;
        this.despachado = despacho;
    }

    public Despacho(int id, Usuario usuario, Atividade atividade, Timestamp despacho, Timestamp inicio, Timestamp termino, boolean finalizadoOk, String obs) {
        this(id,usuario,atividade,despacho);
        this.inicio = inicio;
        this.termino = termino;
        this.finalizadoOk = finalizadoOk;
        this.obs += obs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public Timestamp getDespachado() {
        return despachado;
    }

    public void setDespachado(Timestamp despachado) {
        this.despachado = despachado;
    }

    public Timestamp getDespacho() {
        return despachado;
    }

    public void setDespacho(Timestamp despacho) {
        this.despachado = despacho;
    }

    public Timestamp getInicio() {
        return inicio;
    }

    public void setInicio(Timestamp inicio) {
        this.inicio = inicio;
    }

    public Timestamp getTermino() {
        return termino;
    }

    public void setTermino(Timestamp termino) {
        this.termino = termino;
    }

    public boolean isFinalizadoOk() {
        return finalizadoOk;
    }

    public void setFinalizadoOk(boolean finalizadoOk) {
        this.finalizadoOk = finalizadoOk;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}

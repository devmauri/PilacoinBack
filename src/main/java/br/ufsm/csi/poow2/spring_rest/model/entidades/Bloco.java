package br.ufsm.csi.poow2.spring_rest.model.entidades;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bloco {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;
    @Column(length = 512)
    private byte[] chaveUsuarioMinerador;
    private String nonce;
    private String nonceBlocoAnterior;
    private long numeroBloco;
    @OneToMany(mappedBy = "bloco" , fetch = FetchType.EAGER)
    private List<Transacao> transacoes = new ArrayList<Transacao>();

    public Bloco() {
        transacoes = new ArrayList<Transacao>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getChaveUsuarioMinerador() {
        return chaveUsuarioMinerador;
    }

    public void setChaveUsuarioMinerador(byte[] chaveUsuarioMinerador) {
        this.chaveUsuarioMinerador = chaveUsuarioMinerador;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getNonceBlocoAnterior() {
        return nonceBlocoAnterior;
    }

    public void setNonceBlocoAnterior(String nonceBlocoAnterior) {
        this.nonceBlocoAnterior = nonceBlocoAnterior;
    }

    public long getNumeroBloco() {
        return numeroBloco;
    }

    public void setNumeroBloco(long numeroBloco) {
        this.numeroBloco = numeroBloco;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}

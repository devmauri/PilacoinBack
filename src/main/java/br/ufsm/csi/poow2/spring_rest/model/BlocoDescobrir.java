package br.ufsm.csi.poow2.spring_rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


import java.util.List;


@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlocoDescobrir {

    private long numeroBloco;
    private String nonce;
    private byte[] chaveUsuarioMinerador;
    private byte[] nonceBlocoAnterior;
    private List<TransacaoDescobrir> transacoes;

    public long getNumeroBloco() {
        return numeroBloco;
    }

    public void setNumeroBloco(long numeroBloco) {
        this.numeroBloco = numeroBloco;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public byte[] getChaveUsuarioMinerador() {
        return chaveUsuarioMinerador;
    }

    public void setChaveUsuarioMinerador(byte[] chaveUsuarioMinerador) {
        this.chaveUsuarioMinerador = chaveUsuarioMinerador;
    }

    public byte[] getNonceBlocoAnterior() {
        return nonceBlocoAnterior;
    }

    public void setNonceBlocoAnterior(byte[] nonceBlocoAnterior) {
        this.nonceBlocoAnterior = nonceBlocoAnterior;
    }

    public List<TransacaoDescobrir> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransacaoDescobrir> transacoes) {
        this.transacoes = transacoes;
    }
}

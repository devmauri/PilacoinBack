package br.ufsm.csi.poow2.spring_rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Date;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaValidaRest {

    private byte[] assinaturaMaster;
    private byte[] chaveCriador;
    private Date dataCriacao;
    private Long id;
    private String nonce;
    private String status;

    public byte[] getAssinaturaMaster() {
        return assinaturaMaster;
    }

    public void setAssinaturaMaster(byte[] assinaturaMaster) {
        this.assinaturaMaster = assinaturaMaster;
    }

    public byte[] getChaveCriador() {
        return chaveCriador;
    }

    public void setChaveCriador(byte[] chaveCriador) {
        this.chaveCriador = chaveCriador;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void print(){
        StringBuilder sb = new StringBuilder();
        sb.append("########Pila Print**********\n");
        sb.append("id "+id);
        sb.append("\tnonce "+nonce);
        sb.append("\tstatus "+status);
        sb.append("\tassinatura "+ Base64.encodeBase64String(assinaturaMaster));
        sb.append("\tchaveCriador "+ Base64.encodeBase64String(chaveCriador));
        sb.append("\tdataCriacao "+dataCriacao);

        System.out.println(sb.toString());
    }

}

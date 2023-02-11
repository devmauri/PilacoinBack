package br.ufsm.csi.poow2.spring_rest.model.entidades;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaCoin implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(length = 512)
    private byte[] assinaturaMaster;
    @Column(length = 512)
    private byte[] chaveCriador;
    private Date dataCriacao;
    private String idCriador;
    private String nonce; //utilizar precisão de 128 bits
    private String status;
    @OneToMany(mappedBy = "pila", fetch = FetchType.EAGER)
    private List<Transacao> transacoes = new ArrayList<Transacao>();

    public PilaCoin() {
        transacoes = new ArrayList<Transacao>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCriador() {
        return idCriador;
    }

    public void setIdCriador(String idCriador) {
        this.idCriador = idCriador;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public byte[] getChaveCriador() {
        return chaveCriador;
    }

    public void setChaveCriador(byte[] chaveCriador) {
        this.chaveCriador = chaveCriador;
    }

    public byte[] getAssinaturaMaster() {
        return assinaturaMaster;
    }

    public void setAssinaturaMaster(byte[] assinaturaMaster) {
        this.assinaturaMaster = assinaturaMaster;
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

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    public BigInteger toHash(){
        return this.getHash().abs();
    }

    private BigInteger getHash(){
        byte[] hash = null;
        try {
            String str = this.castPilaToJson();
            hash = castJsonToSHA256(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return new BigInteger(hash);
        }
    }

    public String castPilaToJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(this);
    }

    private byte[] castJsonToSHA256(String json) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(json.getBytes(StandardCharsets.UTF_8));
    }

    public BigInteger nonceToHash() throws NoSuchAlgorithmException {
        return new BigInteger(this.castJsonToSHA256(this.nonce.toString())).abs();
    }

    public PilaCoin clone(){
        var resp = new PilaCoin();
        resp.setStatus(getStatus());
        resp.setId(getId());
        resp.setIdCriador(getIdCriador());
        resp.setDataCriacao(getDataCriacao());
        resp.setAssinaturaMaster(getAssinaturaMaster());
        resp.setNonce(getNonce());
        resp.setChaveCriador(getChaveCriador());
        resp.setTransacoes(getTransacoes());
        return resp;
    }


}

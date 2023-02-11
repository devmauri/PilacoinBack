package br.ufsm.csi.poow2.spring_rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PilaValidaEnvia {

    private byte[] assinatura;
    private byte[] chavePublica;
    private byte[] hashPilaBloco;
    private String nonce;
    //private PilaCoin tipo;
    private String tipo;

    public PilaValidaEnvia(){}
    public PilaValidaEnvia(String nonce) {
        this.nonce = nonce;
    }

    public byte[] getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(byte[] assinatura) {
        this.assinatura = assinatura;
    }

    public byte[] getChavePublica() {
        return chavePublica;
    }

    public void setChavePublica(byte[] chavePublica) {
        this.chavePublica = chavePublica;
    }

    public byte[] getHashPilaBloco() {
        return hashPilaBloco;
    }

    public void setHashPilaBloco(byte[] hashPilaBloco) {
        this.hashPilaBloco = hashPilaBloco;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String ToJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.writeValueAsString(this);
    }


    public byte[] castJsonToSHA256(String json) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(json.getBytes(StandardCharsets.UTF_8));
    }

    public BigInteger toHash() throws NoSuchAlgorithmException, JsonProcessingException {
        return this.getHash().abs();
    }


    private BigInteger getHash() throws JsonProcessingException, NoSuchAlgorithmException {

        String str = this.ToJson();
        byte[] hash = castJsonToSHA256(str);

        return new BigInteger(hash);
    }

    public enum PILA {BLOCO, PILA};
}

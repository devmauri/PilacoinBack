package br.ufsm.csi.poow2.spring_rest.model.entidades;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "LOGINS")
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;

    @Column( unique = true)
    private String nome="";
    @Column (nullable = false)
    private String senha="";
    @Column (nullable = true)
    private String token="";
    //@CreationTimestamp
    @Column (nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaFalha;
    @Column(nullable = false)
    private AUTORIDADE autoridade;

    public Login(){
        this.autoridade = AUTORIDADE.USUARIO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getUltimaFalha() {
        return ultimaFalha;
    }

    public void setUltimaFalha(Date ultimaFalha) {
        this.ultimaFalha = ultimaFalha;
    }

    public AUTORIDADE getAutoridade() {
        return autoridade;
    }

    public void setAutoridade(AUTORIDADE autoridade) {
        this.autoridade = autoridade;
    }

    @Override
    public String toString(){
        var resp = new StringBuilder();
        resp.append("/tNome: " + this.nome);
        resp.append("/tSenha: " + this.senha);
        resp.append("/tToken: " + this.token);
        return resp.toString();
    }

    public void horaFalhaLogin(){
        setUltimaFalha(Timestamp.from(Instant.now()));
    }

    public String senhaToHas(){
        return toHas(this.senha);
    }

    public String toHas(String senha){
        try{
            var md = MessageDigest.getInstance("SHA-256");
            return Base64.encodeBase64String(md.digest(senha.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public enum AUTORIDADE { USUARIO, ADMIN};

}

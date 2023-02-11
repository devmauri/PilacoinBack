package br.ufsm.csi.poow2.spring_rest.model.entidades;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;
import java.util.Date;

@Entity
@JsonPropertyOrder(alphabetic = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true)
    private Long id;
    @ManyToOne ( fetch = FetchType.EAGER)
    @JoinColumn(name = "bloco_id")
    private Bloco bloco;
    @ManyToOne ( fetch = FetchType.EAGER)
    @JoinColumn(name = "pila_id")
    private PilaCoin pila;
    @Column (name = "assinatura")
    private String assinatura;
    @Column
    private Date dataTransacao;
    @Column
    private String status;

    public PilaCoin getPila() {
        return pila;
    }

    public void setPila(PilaCoin pila) {
        this.pila = pila;
    }

    public Bloco getBloco() {
        return bloco;
    }

    public void setBloco(Bloco bloco) {
        this.bloco = bloco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }


    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

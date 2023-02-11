package br.ufsm.csi.poow2.spring_rest.model.entidades;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;


@Entity
public class Log {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date time;

    private String usuario;
    private String metodo;
    private String status;
    private String obs;


    public Log(String usuario, String metodo, STATUS status, String obs) {
        this.usuario = usuario;
        this.metodo = metodo;
        this.status = status.name();
        this.obs = obs;
    }

    public Log() {
        this.usuario = "";
        this.metodo = "";
        this.status = STATUS.INICIADO.name();
        this.obs = "";
        
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getStatus() {
        return this.status ;
    }

    public void setStatus(STATUS status) {
        this.status = status.name();
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }



    public enum STATUS {OK,NOK,INICIADO,PROCESSANDO,FINALIZADO}

    public void addObs(String add){
        if (add == null) return;
        this.obs = this.obs + " // " + add;
    }

}

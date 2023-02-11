package br.ufsm.csi.poow2.spring_rest.dto;


public class RespostaDto<T> {

    private Status status;
    private String msg;
    private T   dado;

    public RespostaDto (){
        this.dado = null;
    }

    public RespostaDto (Status status){
        this();
        this.status = status;
    }

    public RespostaDto(Status status, String msg){
        this(status);
        this.msg = msg;
    }

    public RespostaDto ( Status status, T dado){
        this(status);
        this.dado = dado;
    }

    public RespostaDto(Status status, String msg, T dado){
        this(status, msg);
        this.dado = dado;
    }

    public enum Status {
        SUCESSO, INFORMATIVO, ATENCAO, ERRO
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDado() {
        return dado;
    }

    public void setDado(T dado) {
        this.dado = dado;
    }
}

package br.ufsm.csi.poow2.spring_rest.model;

public class Usuario {

    private Integer id;
    private String nome;
    private Cargo cargo;
    private Senha senha;
    private String token;

    public Usuario (){} //tive que fazer um construtor default por causa do WebSecurity

    public Usuario (String nome, Cargo cargo){
        this.senha = new Senha();
        this.cargo = new Cargo();

        this.nome = nome;
        this.cargo = cargo.copia();
    }

    public Usuario (String nome, Senha senha, Cargo cargo){
        this(nome,cargo);
        this.senha = senha.copia();
    }
    public Usuario (int id, String nome, Senha senha, Cargo cargo){
        this(nome,senha,cargo);
        this.id = id;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Senha getSenha() {
        return senha;
    }

    public void setSenha(Senha senha) {
        this.senha = senha;
    }

    public String getToken() { return token;}

    public void setToken(String token) { this.token = token;}

    public Usuario copia(){
        Usuario temp = new Usuario(this.id
                    ,this.nome
                    ,this.senha
                    ,this.cargo);
        temp.setToken(this.getToken());

        return temp;
    }
}

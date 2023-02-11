package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.repository.UsuarioRepository;
import br.ufsm.csi.poow2.spring_rest.model.UsuarioRest;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceSA {

    @Autowired
    private LogService log;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Value("${endereco.server}")
    private String enderecoServer;
    private String nome = "Otimista-02";
    private KeyPair kp;
    private ChaveService cs;

    public String getUsuarioNome(){
        return this.nome;
    }


    public UsuarioServiceSA(){
        this.cs = new ChaveService();
        this.kp = cs.chavesUsuario();
    }

    public void novoUsuario (Usuario novo){
        var tempL = new Log("ServidorB", "novoUsuario", Log.STATUS.OK,"");
        try{
            usuarioRepository.save(novo);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao criar novo usuario no banco: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
        }
    }

    public List<Usuario> usuarios(){
        var tempL = new Log("ServidorB", "Lista de pilaCoins", Log.STATUS.OK,"");
        List<Usuario> resp = new ArrayList<>();
        try{
            resp = usuarioRepository.findAll();
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Lista de pilaCoins: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }

    public Usuario buscaUsuario(byte[] chave){
        var tempL = new Log("ServidorB", "buscaUsuario", Log.STATUS.OK,"");
        var resp = new Usuario();
        try{
            resp = usuarioRepository.userByKey(chave);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar PilaCoin especifico no banco de dados local: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            log.registrar(tempL);
            return resp;
        }
    }

    public KeyPair chavesUsuario(){
        if(this.kp ==null){
            this.kp = cs.chavesUsuario();
        }

        return this.kp;
    }


    @PostConstruct
    public void init() {
        System.out.println("Usuario Service iniciado");
        log.registrar(new Log("ServidorB", "Usuario.Service.init",Log.STATUS.OK,""));
        registraUsuarioSA(nome, this.chavesUsuario());
    }

    public UsuarioRest registraUsuarioSA(String nome, KeyPair kp) {
        var tempL = new Log("ServidorB", "Registra Usuario Servidor A", Log.STATUS.OK,"");
        UsuarioRest usuarioRest = usuarioSA(kp.getPublic().getEncoded());
        if(usuarioRest !=null) {
            System.out.println("usuario já cadastrado.");
            return usuarioRest;
        }

        usuarioRest = new UsuarioRest(nome, kp.getPublic().getEncoded());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UsuarioRest> entity = new HttpEntity<>(usuarioRest, headers);
        RestTemplate restTemplate = new RestTemplate();
        var temUR = new UsuarioRest();
        try {
            ResponseEntity<UsuarioRest> resp = restTemplate.postForEntity("http://" + enderecoServer + "/usuario/", entity, UsuarioRest.class);
            temUR = resp.getBody();
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao cadastrar usuario: " + e.getMessage());
            System.out.println(tempL.getObs());
            temUR=null;
        }
        finally {
            log.registrar(tempL);
            return temUR;
        }
    }

    public UsuarioRest usuarioSA (byte[] kp){
        var temp = new Log("ServidorB", "Retorna Usuario Servidor A", Log.STATUS.INICIADO,"");
        try {
            var usuario = new UsuarioRest(kp);
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            var entity = new HttpEntity<>(usuario, headers);
            var restTemplate = new RestTemplate();

            var resp = restTemplate.postForEntity("http://" + enderecoServer + "/usuario/findByChave", entity, UsuarioRest.class);
            temp.setStatus(Log.STATUS.OK);
            temp.setObs("pego usuario " + resp.getBody().getNome());
            log.registrar(temp);
            return resp.getBody();
        } catch (Exception e){
            temp.setStatus(Log.STATUS.NOK);
            temp.setObs("Falha ao pegar usuario: " + e.getMessage());
            log.registrar(temp);
            System.out.println(temp.getObs());
            return null;
        }

    }

    public List<UsuarioRest> listaUsuariosSA(){
        var tempL = new Log("ServidorB", "Lista Usuarios do Servidor A", Log.STATUS.INICIADO,"");
        List<UsuarioRest> resp = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<UsuarioRest[]> responseEntity = restTemplate.getForEntity("http://" + enderecoServer + "/usuario/all", UsuarioRest[].class);
            for (var item : responseEntity.getBody() ) {
                resp.add(item);
            }
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Lista de usuarios: " + e.getMessage());
            System.out.println(tempL.getObs());
        }
        finally {
            log.registrar(tempL);
            return resp;
        }
    }

}

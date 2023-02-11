package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Login;
import br.ufsm.csi.poow2.spring_rest.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private LogService logService;


    public Login buscaLogin(String nome){
        var tempL = new Log("ServidorB - "+nome, "BuscaLogin", Log.STATUS.OK,"");
        var resp = new Login();
        try{
            resp = loginRepository.loginByNome(nome);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Login especifico no banco de dados local: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            logService.registrar(tempL);
            return resp;
        }
    }

    public void novoLogin (Login novo){
        var tempL = new Log("ServidorB", "Cria novo Login no banco", Log.STATUS.OK,"");
        try{
            var has = novo.senhaToHas();
            novo.setSenha(has);
            loginRepository.save(novo);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao criar novo loginno banco: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            logService.registrar(tempL);
        }
    }

    public List<Login> logins(){
        var tempL = new Log("ServidorB", "Lista de logins", Log.STATUS.OK,"");
        List<Login> resp = new ArrayList<>();
        try{
            resp = loginRepository.findAll();
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha ao pegar Lista de logins: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            logService.registrar(tempL);
            return resp;
        }
    }

    private void falhaNoLogin(Login login){
        if(login.getNome()==null) {
            return;
        }
        var tempL = new Log("ServidorB", "falhaNoLogin", Log.STATUS.OK,login.getNome());
        try{
            var temp = this.buscaLogin(login.getNome());
            temp.horaFalhaLogin();
            loginRepository.save(temp);
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha registar falhaNoLogin: " + e.getMessage());
            System.out.println(tempL.getObs());
        }finally {
            logService.registrar(tempL);
        }
    }

    public Boolean fazLogin(Login login){
        var resp = false;
        if(login.getNome()==null || login.getSenha()==null) {
            return false;
        }
        var tempL = new Log("ServidorB", "fazLogin", Log.STATUS.OK,login.getNome());
        try{
            var temp = this.buscaLogin(login.getNome());
            if(temp == null || temp.getSenha()==null){//login inexistente
                return false;
            }
            else if (temp.getUltimaFalha() !=null){
                var dif = Timestamp.from(Instant.now()).getTime() - temp.getUltimaFalha().getTime();
                if(dif < 3000){//bloqueia por 3 segundos devido a tentativas seguidas
                    return false;
                }
            }
            var has = login.senhaToHas();
            if(has.equals(temp.getSenha())){
                resp=true;
            }
            else {
                resp=false;
                falhaNoLogin(login);
            }
        } catch (Exception e) {
            tempL.setStatus(Log.STATUS.NOK);
            tempL.setObs("Falha registar falhaNoLogin: " + e.getMessage());
            System.out.println(tempL.getObs());
            resp = false;
        }finally {
            logService.registrar(tempL);
            return resp;
        }
    }
}

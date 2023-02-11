package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.model.*;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PilaService {
    @Autowired
    private LogService logService;
    @Autowired
    private UsuarioServiceSA usuarioServiceSA;
    private MineracaoService ms;

    private SecureRandom rnd;
    private BigInteger dificuldade;
    private PilaCoin pila;
    private int maxThread = 4;
    private List<PilaRun> threads;

    private boolean run=false;

    public PilaService(){
        this.pila = new PilaCoin();
        this.rnd = new SecureRandom();
        this.threads = new ArrayList<PilaRun>();
    }

    public void informaMinerador(MineracaoService minerador){
        this.ms = minerador;
    }

    public BigInteger getDificuldade(){return this.dificuldade;}
    public boolean isMinerando(){
        return run;
    }
    private void iniciadoMineracao(){
        run = true;
    }
    public String statusMineracao(){
        var resp = new StringBuilder();
        if(isMinerando()){
            resp.append("MINERANDO tche");
        }
        else{
            resp.append("Mineração parada.");
        }
        return resp.toString();
    }
    public void atualizaDificuldade(BigInteger dificuldade){
        if(!dificuldade.equals(this.dificuldade)){
            var temp = isMinerando();
            this.pararMineracao();
            this.dificuldade = dificuldade;
            if(temp) {
                this.startMineracao();
            }
        }
    }
    public PilaCoin gerarPila(Date criacao, byte[] chaveCriador, String nonce){
        var temp = new PilaCoin();
        temp.setChaveCriador(chaveCriador);
        temp.setDataCriacao(criacao);
        temp.setNonce(nonce);
        return temp;
    }
    public PilaCoin gerarPilaRandomico(){
        //gera numero aleatorio para chaves
        return this.gerarPila(new Date(),
                usuarioServiceSA.chavesUsuario().getPublic().getEncoded(),
                new BigInteger(128, rnd).abs().toString());
    }
    public byte[] assinatura(PilaValidaEnvia pila) throws
                    NoSuchPaddingException, NoSuchAlgorithmException, JsonProcessingException,
                    IllegalBlockSizeException, BadPaddingException, InvalidKeyException
    {
        Cipher cipherAES = Cipher.getInstance("RSA");
        cipherAES.init(Cipher.ENCRYPT_MODE, usuarioServiceSA.chavesUsuario().getPrivate());
        return cipherAES.doFinal(pila.toHash().toByteArray());
    }
    public byte[] assinatura(PilaCoin pila) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipherAES = Cipher.getInstance("RSA");
        cipherAES.init(Cipher.ENCRYPT_MODE, usuarioServiceSA.chavesUsuario().getPrivate());
        return cipherAES.doFinal(pila.toHash().toByteArray());
    }
    public void minerarBloco(BlocoDescobrir bloco){ }//retirei pq parou de vir bloco
    public void validaTransacao(TransacaoRest transacaoRest){ }//retirei pq nunca venho transação

    public void validarPilaOutroUsuario(PilaValidaRest pilaValidar){
        //gera novo pila e compara nonce, se ok assina e envia para servidor
        try{
            var pila = gerarPila(pilaValidar.getDataCriacao(),
                    pilaValidar.getChaveCriador(),pilaValidar.getNonce());
            if( pila.toHash().compareTo(dificuldade) <0){
                //ok
                pila.setStatus("Valido");
                PilaValidaEnvia enviar = new PilaValidaEnvia(pilaValidar.getNonce());
                enviar.setTipo("PILA");
                enviar.setChavePublica(usuarioServiceSA.chavesUsuario().getPublic().getEncoded());
                var str = enviar.ToJson();
                var sha = enviar.castJsonToSHA256(str);
                var bg = new BigInteger(sha).abs();
                enviar.setHashPilaBloco(sha);
                var asstmp = assinatura(enviar);

                ms.confirmouPilaOutroUsuario(enviar);
            }
            else{
                //falso, não envia.
                pila.setStatus("Invalido");
            }
        } catch (Exception e) {
            var tempL = new Log("ServidorB", "validarPilaOutroUsuario", Log.STATUS.NOK,"Erro ao validar pila de outro usuario: ");
            tempL.addObs(e.getMessage());
            logService.registrar(tempL);
        }
    }


    public String startMineracao(){
        var resp = new StringBuilder();
        if(dificuldade == null){
            resp.append("Não iniciado porque servidor A ainda não enviou dificuldade.");
        }
        else if(!isMinerando()){
            resp.append("Iniciado Mineração");
            iniciadoMineracao();
            //startar trheads e armazenar
            synchronized (this.threads){
                for (int i =this.threads.size(); i<maxThread;i++){
                    this.threads.add(new PilaRun(this));
                    System.out.println("\tADICIONADO NOVA THREAD: "+i);
                }
            }
            run = true;
        }
        else{
            resp.append("Já minerando. Acalme!");
        }

        return resp.toString();
    }

    public void minerou (PilaRun vencedor) {
        System.out.println("\n\n\n########## Minerou ###########\n\n");
        synchronized (vencedor){
            this.pila = vencedor.pilaMinerado().clone();
        }
        var tempL = new Log("ServidorB", "startMineracao", Log.STATUS.OK,"########## Minerou ###########");
        tempL.addObs(" Pila -> " + pila.toHash());
        logService.registrar(tempL);
        ms.enviarPilaCoinParaValidacao(pila);
        //pararMineracao();
    }


    public String pararMineracao(){
        var resp = new StringBuilder();
        if(isMinerando()){
            resp.append("Mineração Parada");
            //parar threads
            synchronized (this.threads){
                while (this.threads.size()>0 ) {
                    System.out.println("REMOVENDO THREAD "+this.threads.size());
                    this.threads.get(0).parar();
                    this.threads.remove(this.threads.get(0));
                }
            }
            run = false;
        }
        else{
            resp.append("Já não estava minerando. Relaxa.");
        }
        return resp.toString();
    }
}

package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.dao.ChavesDiscoDAO;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class ChaveService {

    public KeyPair geraParChaves(int keysize) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(keysize);
        return kpg.generateKeyPair();
    }

    public PrivateKey castByteArrayToPrivateKey(byte[] bArray) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return  kf.generatePrivate(new PKCS8EncodedKeySpec(bArray));
    }

    public PublicKey castByteArrayToPublicKey(byte[] bArray) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(new X509EncodedKeySpec(bArray));
    }

    public KeyPair chavesUsuario(){
        KeyPair kp = null;

        try {
            //busca o par de chaves salva no disco
            if(new ChavesDiscoDAO().chavesExistem()){
                byte[] bkprivada = new ChavesDiscoDAO().buscaChavePrivadaDisco();
                byte[] bkpublica = new ChavesDiscoDAO().buscaChavePublicaDisco();

                PrivateKey privateKey = this.castByteArrayToPrivateKey(bkprivada);
                PublicKey publicKey = castByteArrayToPublicKey(bkpublica);
                kp = new KeyPair(publicKey,privateKey);

            }else{
                // Caso não tenha chave salva gera e escrever no disco pq a chave puplica será o numero da carteira
                kp = this.geraParChaves(2048);
                new ChavesDiscoDAO().salvaChavePrivadaDisco(kp.getPrivate().getEncoded());
                new ChavesDiscoDAO().salvaChavePublicaDisco(kp.getPublic().getEncoded());
            }
            if(!chavesOk(kp)){System.out.println("Erro na geração das chaves");}
        }
        catch (Exception e){

        }

        return kp;
    }

    public PublicKey chaveProfessor(){
        PublicKey pb = null;

        try {
            if(new ChavesDiscoDAO().chaveProfessorExiste()){
                pb = this.castByteArrayToPublicKey(new ChavesDiscoDAO().chaveProfessor());
            }
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return pb;
    }

    public boolean chavesOk(KeyPair kp){ // verifica se chaves são iguais a do disco.
        return (
                Base64.getEncoder().encodeToString(new ChavesDiscoDAO().buscaChavePrivadaDisco())
                    .equals(Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()))
                && Base64.getEncoder().encodeToString(new ChavesDiscoDAO().buscaChavePublicaDisco())
                    .equals(Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()))
                );
    }


}

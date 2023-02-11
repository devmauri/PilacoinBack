package br.ufsm.csi.poow2.spring_rest.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChavesDiscoDAO {

    private String CHAVE_PUBLICA= ".\\publictekeyJson.txt";
    private String CHAVE_PRIVADA = ".\\privatekeyJson.txt";
    private String CHAVE_PUBLICA_PROFESSOR = ".\\master-pub.key";

    public boolean existeArquivo(String caminho){
        return new File(caminho).exists();
    }

    public boolean chavesExistem(){
        return (this.existeArquivo(CHAVE_PRIVADA) && this.existeArquivo(CHAVE_PUBLICA));
    }

    public boolean chaveProfessorExiste(){
        return this.existeArquivo(CHAVE_PUBLICA_PROFESSOR);
    }

    public byte[] buscaChaveDisco(String nome) {
        try {
            return Files.readAllBytes(Path.of(nome));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] buscaChavePrivadaDisco(){
        return this.buscaChaveDisco(CHAVE_PRIVADA);
    }

    public byte[] buscaChavePublicaDisco(){
        return this.buscaChaveDisco(CHAVE_PUBLICA);
    }

    public byte[] chaveProfessor(){
        return this.buscaChaveDisco(CHAVE_PUBLICA_PROFESSOR);
    }

    public void salvaChaveDisco(byte[] chaveJsonByte, String nome) {
        try {
            FileOutputStream keiPairArquivo = new FileOutputStream(nome);
            keiPairArquivo.write(chaveJsonByte);
            keiPairArquivo.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void salvaChavePrivadaDisco(byte[] chaveJsonByte){
        this.salvaChaveDisco(chaveJsonByte, CHAVE_PRIVADA);
    }

    public void salvaChavePublicaDisco(byte[] chaveJsonByte){
        this.salvaChaveDisco(chaveJsonByte, CHAVE_PUBLICA);
    }




}

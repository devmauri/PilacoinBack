package br.ufsm.csi.poow2.spring_rest.model;

import br.ufsm.csi.poow2.spring_rest.model.entidades.PilaCoin;
import br.ufsm.csi.poow2.spring_rest.service.PilaService;

import java.math.BigInteger;

public class PilaRun implements Runnable{
    private BigInteger dificuldade;

    private PilaCoin pila;
    private Thread th;
    private PilaService ps;

    public PilaRun (PilaService pilaService){
        pila = new PilaCoin();
        this.ps = pilaService;
        this.dificuldade = pilaService.getDificuldade();
        this.th = new Thread(this);
        th.start();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()
                + ", iniciou ###########");
        this.pila = this.loop();
        System.out.println(Thread.currentThread().getName()
                + ", terminou ##########");
        ps.minerou(this);
    }

    private PilaCoin loop (){
        PilaCoin resp = null;
        try{
            boolean sair = false;
            //para análise de tempo medio para calcular o pila
            long tempoInicial = System.currentTimeMillis();
            long periodo = 120*1000;
            long ultimaMedida = System.currentTimeMillis();
            long iteracao=0;
            PilaCoin pilaCoin = new PilaCoin();

            int avgId=0;
            final int avgMaxId=300;
            long[] avgStore = new long[avgMaxId];
            long ultima=0, agora ;
            float avgCiclo=0,soma=0;

            do {
                agora = System.nanoTime();
                soma -= avgStore[avgId];
                avgStore[avgId]= agora - ultima;
                ultima = agora;
                soma += avgStore[avgId];
                avgId = (avgId<(avgMaxId-1)) ? avgId++ : 0;
                avgCiclo = soma/avgMaxId;

                //gera novo pila
                pilaCoin = ps.gerarPilaRandomico();
                //converte pila -> json -> sha256 -> biginteger -> valor bsoluto
                BigInteger numHash = pilaCoin.toHash();

                if (numHash.compareTo(this.dificuldade) < 0) {
                    sair = true;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Pilou $$$$$$$$$$$$$$$$$$$$");
                    sb.append( "\tMédia por ciclo "+avgCiclo+" ns. Tempo total " + (System.currentTimeMillis() - tempoInicial)/1000f + " s." );
                    sb.append("\n\tIteração "+iteracao);
                    sb.append("\t Pila: "+ pilaCoin.castPilaToJson());
                    sb.append("\n\tDificuldade:\n"+ this.dificuldade);
                    sb.append("\n\tHas sha256 gerada:\n"+ numHash);
                    System.out.println(sb.toString());

                } else {
                    if(System.currentTimeMillis()- ultimaMedida > periodo){
                        ultimaMedida = System.currentTimeMillis();
                        StringBuilder sb = new StringBuilder();
                        sb.append("Thread\t"+ this.th.getName()+"\tIteração "+iteracao+"\tMédia do ciclo "+avgCiclo);
                        System.out.println(sb.toString());
                    }
                }
                iteracao++;
            }while (!sair);
            //só sao do while quando minerar
            resp = pilaCoin;

        }catch (Exception e){
        }
        return resp;
    }

    public PilaCoin pilaMinerado(){
        return this.pila;
    }

    public boolean terminou(){
        return !this.th.isAlive();
    }

    public void parar(){
        this.th.stop();
    }
}

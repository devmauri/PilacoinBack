package br.ufsm.csi.poow2.spring_rest.model.events;

import java.util.ArrayList;
import java.util.List;


public class RecebimentoWSC {
    //modelo criado para recebimento de dados via websocket
    private static List<EventoRecepcao> ouvintes = new ArrayList<>();
    private static RecebimentoWSC singleton;

    private RecebimentoWSC(){
        singleton = this;
    }

    public static RecebimentoWSC Singleton(){
        if(singleton == null){
            new RecebimentoWSC();
        }
        return singleton;
    }

    public void addOuvinte ( EventoRecepcao ouvinte){
        synchronized (ouvintes) {ouvintes.add(ouvinte);}
    }

    public void remOuvinte( EventoRecepcao ouvinte){
        synchronized (ouvintes) {ouvintes.remove(ouvinte);}
    }
    public void invokeAcoes(EVENTOS acoes, Object... args){
        for (EventoRecepcao ouvinte : ouvintes ) {
            ouvinte.recebidoWSC(acoes,args);
        }
    }

    protected void finalize(){
        ouvintes.clear();
    }
    public enum EVENTOS{ DIFICULDADE, PILA_ENVIADO, PILA_VALIDAR, BLOCO_MINERAR, BLOCO_VALIDAR};
    public interface EventoRecepcao{
        void recebidoWSC(EVENTOS acoes, Object... args);
    }
}



package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.model.entidades.Log;
import br.ufsm.csi.poow2.spring_rest.repository.LogRepository;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LogService {
    @Autowired
    private LogRepository logBD;
    @PostConstruct
    public void iniciou(){
        //this.logBD = (LogRepository) new Log();
        var temp = new Log("Sistema","LogService.iniciou",Log.STATUS.INICIADO,"");
        this.logBD.save(temp);
    }

    public void registrar(Log log) {
        this.logBD.save(log);
    }
    
    public List<Log> allLogs() {
        return this.logBD.findAll();
    }
}

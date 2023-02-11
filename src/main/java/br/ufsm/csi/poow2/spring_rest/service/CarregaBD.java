package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.model.entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class CarregaBD {
    //classe para auxiliar nos testes. Cria dados.
    @Autowired
    private LogService logService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private BlocoService blocoService;
    @Autowired
    private PilaCoinService pilaCoinService;
    @Autowired
    private TransacaoService transacaoService;
    public static List<PilaCoin> pilas;


    @PostConstruct
    public void testeLog(){
        logService.registrar(new Log("mock","testeLog", Log.STATUS.OK,"sem obs"));
        System.out.println(logService.allLogs().get(logService.allLogs().size()-1).getObs());
    }
    @PostConstruct
    public void criaTransacoes(){

        var bloco = new Bloco();
        bloco.setChaveUsuarioMinerador("Kna7AbvdOVjQsDaNNDSdFU55Nt2bxrcQN6LN4PSq7BDS+nb1ksCYOhuFfaPtDjoHHCzIL9S7r4HqtdhiW2ijc5pAbBZ8porHVPGDZqxTxR03gS82JJReNPtknR+DuiZSTWlDI3wz4BHFCGgohSQA2CqfAK/lzch5HOxsGHoIgSHrfc7IZgUupOjuPee4wXPVSVnHIqJVwbNsVUiHvhqb/jWQIcnor6Hi5Xg0P5KvLu3KNjmxR5TkDPJ1iGXG8kw8GiJw5JbhGU94yVGmlac/QhQJIiNNJHrFOnzZ4LDthVX8AfDVA7cxAV46595AyNY2BQyyHaHNXMTVC+jZnvsf3w==".getBytes(StandardCharsets.UTF_8));
        bloco.setNonce("82722903657502152400237555063135160169") ;
        bloco.setNumeroBloco(53);
        bloco.setNonceBlocoAnterior("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAviCGmjemDU/qL8yb4VGYwO0smdZWr3iC5QRNz2FH1T/ppBQL5WH64N89XkeGZ3rHite/5EtONsUQaMJtkyUf9RTU1uzfqubdACViUyVVJf1Zdjlc8WOk4Th9vLLrbTIxcYvcwaAJXRYeb3M1wiFUq8uOD+9p0GzCoS76W76uR6COXwUcNMdlNTD/f+6NTnbat7tUQvqhbLaNXydnQeUZT9P5eaPmaBItnqOjwdl1SMKnPkSOzvfFO6CdrySvNQGq9W6O+7kxKC7jmtm3N49AnJKxFenKMcrcBehiNm6059c+urJ3+ZtZqT8WP3+izyhfGecCc3WKfGnI2VSAGHOGZQIDAQAB");
        blocoService.novoBloco(bloco);
        bloco.setId(null);
        bloco.setNonce("205735508011312958674123365825565775502") ;
        bloco.setNumeroBloco(bloco.getNumeroBloco()+1);
        bloco.setNonceBlocoAnterior(bloco.getNonce());
        blocoService.novoBloco(bloco);
        pilas = new ArrayList<>();
        var pila = new PilaCoin();
        pila.setAssinaturaMaster("KA4+dyVIAqox/Erq466Xf77JNdiCorsVcGZuEaQ+bzvqZ3GiKcSzQe4bwYLgJrWsvyAqVRwhnmNvC2YsPKyK7Poot4MM5uvVZyKCPmEvHEn3kLd5oUzo04VC3WLmB1fmOk/Vy6aqHhMHBOQGU4DGRaqpS82gCfMAvdBRMruqi3sXFwGpHT7k1CyCKOcEc4I/mfmcm8NvazFE/ZZ+EbhXd3vwZXl1zDInhGn1cfeOWELC36Bp6w1ZVG9z30fhxHPA6Wh1Ab6n3x2907RJ6iMzA3Fo7YKchjIS8Gjqfcauu7leJPlN4k4YkeBSUSQBz4uqecFOMZGD03tmuDi6uiVidA==".getBytes(StandardCharsets.UTF_8));
        pila.setChaveCriador("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwEAdcv0F1+ij//QhUh3GBtay6MIKg3M7ZB73vMt6BbnDxFgqqtHHccxlHkjsrJVl5byCiD2VxwbzJSzz/rzrcd3Nw4oug1/N0OB+i/vcrKOrOSeLRyi073uGGhq+82LOwOLjJqEYGCNoFzT1fSySyn5EEabKyD2xfGhT0aJ7JdkObzTHhFXket7ddVFQeg6Lty3Y8ZzOcsdOlKnhBO+4xmeFKGmPbE5JWZv12+Yg/W/S+HDvUm5AiBzR31kPtyQIcxGK5rAl5B2RWfc22t6oETfvFzAU61ByVTZ5jrDWOErEhShrHjSPis1w3uCxYL7FB5in87dtHpXzZfHyjjAhgwIDAQAB".getBytes(StandardCharsets.UTF_8));
        pila.setDataCriacao(Time.from(Instant.now()));
        pila.setIdCriador("223007");
        pila.setNonce("247361850310804960237192765982748201420");
        pila.setStatus("AG_VALIDACAO");
        pilaCoinService.novoPila(pila);
        pilas.add(pila);
        pila = new PilaCoin();
        pila.setDataCriacao(Time.from(Instant.now()));
        pila.setIdCriador("223007");
        pila.setStatus("AG_VALIDACAO");
        pila.setAssinaturaMaster( "JDlMhffBxsWB01TVY0X8VrYWqYRNKdQbnzSGImyeSRs4X/c6/kbWYgCXZcOr3NrGIOzrMErhRM3aVTlWF5y3AMLrrpGWt+KP6eLXgSM1kekMSzpUb4MeLue8OhKVdPz81iHa9RWlOZBC6iP0OlLbF30Mr1HCGmK/XmfNhBka/crEHHdJEBGHRSl3CM9YukTLnCTMM2O1cCHwuw9paO7EdzvcbDHx6Pcm/h9mrTNvwX8XNN3Dn1vjJ0TRTN0INwKOh9FOEM3LcDYfGMZmg78LS4M0+HWiozCe6hV2jALm8bdB8emL6n00qlk3A0sZM/N7FmYP6pQvL1VDH626MTyQTg==" .getBytes(StandardCharsets.UTF_8));
        pila.setNonce("333980559238913457542819795205137454404");
        pilaCoinService.novoPila(pila);
        pilas.add(pila);
        pila = new PilaCoin();
        pila.setDataCriacao(Time.from(Instant.now()));
        pila.setIdCriador("223007");
        pila.setStatus("AG_VALIDACAO");
        pila.setAssinaturaMaster( "TkhZRsHVzcOLGmldS/Ky08jAe+6f5W3LrQ2spQFXTcXMXs68ytesFYNFioeJD4l9qQ2t7K67uu2LfHxf5+YD5C9B3BMvx7EHpJj9JqSTVWwqM+v2d8PK9NnLCu25yv393zTvoKnh1bAW+fVIYUpCot8J5lzYVvnZhhBnWZJGlNMmRQJwaBlGLN4U4dZKzvP8V5R1KkEBQtCSrCEFpOdqFnodU37SQU66VA8mQDxuuzpOkP3rk683ZrK04UJoBK+W+W7KivUuYw4AvQxgUAf3i4xCvjn0sPX9x7Jdr0mN60J4VvzoozAr9rcLy9kvMrCw03XyYVOnl4efvlXCqq0uFA==" .getBytes(StandardCharsets.UTF_8));
        pila.setNonce("95723508900400621221025254694918612931");
        pilaCoinService.novoPila(pila);
        pilas.add(pila);
        var transacao = new Transacao();
        transacao.setAssinatura("150287057200301555476816997768027249097");
        transacao.setBloco(bloco);
        transacao.setPila(pila);
        transacao.setDataTransacao(Time.from(Instant.now()));
        transacao.setStatus("AG_VALIDACAO");
        transacaoService.novaTransacao(transacao);

    }

    @PostConstruct
    public void criaLoginsLocais(){
        var temp = new Login();
        temp.setNome("Usuario1");
        temp.setSenha("senha1");
        loginService.novoLogin(temp);
        temp.setId(null);
        temp.setNome("Usuario2");
        temp.setSenha("senha2");
        loginService.novoLogin(temp);
        temp.setSenha("senha2");
        temp.setId(null);
        System.out.println(temp);
        var teste = loginService.fazLogin(temp);
        System.out.println("Teste de login valido = " + teste);
        temp.setNome("Usuario1");
        temp.setSenha("asdf");
        System.out.println(temp);
        teste = loginService.fazLogin(temp);
        System.out.println("Teste de login invalido = " +teste);

        var asdf = loginService.logins();
        for (var item  : asdf) {
            System.out.println(item);
        }
    }
}

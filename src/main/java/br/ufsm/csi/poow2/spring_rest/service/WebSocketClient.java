package br.ufsm.csi.poow2.spring_rest.service;

import br.ufsm.csi.poow2.spring_rest.service.Handler.MyStompSessionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import java.math.BigInteger;

@Component
public class WebSocketClient {
    @Value("${endereco.server}")
    private String enderecoServer;
    private MyStompSessionHandler sessionHandler;
    @Autowired
    private LogService log;

    public WebSocketClient(){
        System.out.println("WebSocketClient iniciado");
    }

    @PostConstruct
    private void init() {
        sessionHandler =  new MyStompSessionHandler(log);
        System.out.println("iniciou");
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompClient.connect("ws://" + enderecoServer + "/websocket/websocket", sessionHandler);
        System.out.println("conectou em " + "ws://" + enderecoServer + "/websocket/websocket");
    }
}

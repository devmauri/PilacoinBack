package br.ufsm.csi.poow2.spring_rest.controller;

import br.ufsm.csi.poow2.spring_rest.model.Senha;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;
import br.ufsm.csi.poow2.spring_rest.model.entidades.Login;
import br.ufsm.csi.poow2.spring_rest.security.JWTUtil;
import br.ufsm.csi.poow2.spring_rest.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<Object> autenticacao (
            @RequestBody Usuario usuario){
        System.out.println("autenticacao ");
        Login l = new Login();
        l.setSenha(usuario.getSenha().getSenha());
        usuario.getSenha().setSenha(l.senhaToHas());

        try {

            final Authentication auth = this.authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    usuario.getNome(),usuario.getSenha().getSenha()
                            )
                    );

            if(auth.isAuthenticated()){
                //guarda instancia autenticada
                SecurityContextHolder.getContext().setAuthentication(auth);
                //gera o tokem e encaminha
                usuario.setToken(new JWTUtil().geraToken(usuario));
                usuario.setSenha(new Senha()); //limpa senha

                return new ResponseEntity<>(usuario, HttpStatus.OK);
            }
            return new ResponseEntity<>("Usuario e senha não bateram", HttpStatus.BAD_REQUEST);

        }catch ( Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(" Exeção em autenticacao -> Usuario e senha não bateram", HttpStatus.BAD_REQUEST);
        }

    }
}

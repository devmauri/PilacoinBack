package br.ufsm.csi.poow2.spring_rest.security;

import br.ufsm.csi.poow2.spring_rest.dao.UsuarioDAO;
import br.ufsm.csi.poow2.spring_rest.model.Cargo;
import br.ufsm.csi.poow2.spring_rest.model.Senha;
import br.ufsm.csi.poow2.spring_rest.model.Usuario;
import br.ufsm.csi.poow2.spring_rest.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceCuston implements UserDetailsService {
    @Autowired
    private LoginService loginService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername");
        Usuario usuario = new Usuario();
        var temp = loginService.buscaLogin(username);
        if(temp ==null){
            throw new UsernameNotFoundException("Usuário ou senha inválidos!");
        }
        System.out.println(temp.getNome());
        System.out.println(temp.getSenha());

        var s = new Senha();
        s.setSenha(new BCryptPasswordEncoder().encode(temp.getSenha()));
        //s.setSenha(temp.getSenha());
        var c = new Cargo();
        c.setDescricao(temp.getAutoridade().name());
        c.setNome(temp.getAutoridade().name());
        usuario.setNome(temp.getNome());
        usuario.setSenha(s);
        usuario.setCargo(c);

        System.out.println(usuario.getNome());
        System.out.println(usuario.getSenha().getSenha());


        UserDetails user = User.withUsername(usuario.getNome())
                .password(usuario.getSenha().getSenha())
                .authorities(usuario.getCargo().getNome()).build();
        return user;
    }

}

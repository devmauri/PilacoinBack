package br.ufsm.csi.poow2.spring_rest.security;

import br.ufsm.csi.poow2.spring_rest.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    public static final long TEMPO_VIDA = Duration.ofSeconds(300).toMillis();

    public String geraToken(Usuario usuario){
        System.out.println("geraToken");
        final Map<String, Object> claims = new HashMap<>();
        if(usuario.getNome() !=null){
            claims.put("sub",usuario.getNome());
        }
        if(usuario.getCargo().getNome() !=null){
            claims.put("permissoes:",usuario.getCargo().getNome());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis()+TEMPO_VIDA))
                .signWith(SignatureAlgorithm.HS256, "chave")
                .compact();
    }

    private Claims parseToken (String token){
        return Jwts.parser().setSigningKey("chave")
                .parseClaimsJws(token.replace("Bearer",""))
                .getBody();
    }

    public String getUsernameToken(String token){
        return (token !=null)
                ? this.parseToken(token).getSubject()
                : null;
    }

    public boolean isTokenExpirado (String token){
        return (token !=null)
                ? this.parseToken(token).getExpiration().before(new Date())
                : false;
    }
}

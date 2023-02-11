package br.ufsm.csi.poow2.spring_rest.security;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FiltroAutenticacao extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String url = request.getRequestURI();
        System.out.println("doFilterInternal " + url);

        try {
            if(!url.contains("login")){
                String token = request.getHeader("Authorization");
                String username = new JWTUtil().getUsernameToken(token);
                System.out.println(username);
                if(username !=null && SecurityContextHolder.getContext().getAuthentication() ==null){

                    UserDetails userDT = this.userDetailsService.loadUserByUsername(username);
                    if (! new JWTUtil().isTokenExpirado(token)){
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDT,null,userDT.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request,response);
        }catch (ExpiredJwtException esp){
            System.out.println("Tokem expirado.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        }
    }
}

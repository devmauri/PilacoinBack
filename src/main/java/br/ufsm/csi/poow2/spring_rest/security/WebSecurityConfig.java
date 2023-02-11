package br.ufsm.csi.poow2.spring_rest.security;

import br.ufsm.csi.poow2.spring_rest.model.entidades.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAutenticacao(AuthenticationManagerBuilder auth) throws Exception{
        System.out.println("configureAutenticacao");
        auth.userDetailsService(this.userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        System.out.println("authenticationManager");
        return super.authenticationManager();
    }

    @Bean
    public FiltroAutenticacao filtroAutenticacao(){
        return new FiltroAutenticacao();
    }

    @Override
    protected  void configure (HttpSecurity http) throws Exception {
        System.out.println("configure");
            http.cors().and()
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //.authenticationProvider(this.authProvider())
                    .authorizeHttpRequests()
                    .antMatchers(HttpMethod.GET ,"/ping").permitAll()
                    .antMatchers(HttpMethod.POST ,"/login").permitAll()

                    .antMatchers(HttpMethod.GET, "/carteira/listar").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.GET, "/carteira/pila").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.GET, "/carteira/eventos").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.GET, "/carteira/transferir").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.PUT, "/carteira/transferir").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.GET, "/carteira/statusMineracao").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.POST, "/carteira/stop").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name())
                    .antMatchers(HttpMethod.POST, "/carteira/start").hasAnyAuthority(Login.AUTORIDADE.USUARIO.name(), Login.AUTORIDADE.ADMIN.name());



        http.addFilterBefore(this.filtroAutenticacao(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CorsFilter corsFilter(){
        System.out.println("CorsFilter");
        final var config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",config);

        return new CorsFilter(source);
    }
}

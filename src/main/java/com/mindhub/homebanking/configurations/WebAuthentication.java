package com.mindhub.homebanking.configurations;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    // inyecciones
    @Autowired
    private ClientRepo clientRepo;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    // metodos
    @Override
    public void init (AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(email -> {
            Client client = clientRepo.findByEmail(email);
            if (client != null) {
                if (client.getEmail().equals("zeta@zeta.com")) {
                    // creo la SESION p/ el 'client' ADMINISTRADOR
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN", "CLIENT"));
                } else {
                    // creo la SESION p/ el 'client' AUTENTICADO con ROL de "CLIENT"
                    return new User(client.getEmail(), client.getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }
            } else {
                throw new UsernameNotFoundException("UNKNOWN user: " + email);
            }
        });
    }


}

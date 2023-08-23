package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure (HttpSecurity http) throws Exception {

        // reglas de autorizacion p/ c/ ruta
        http.authorizeRequests().antMatchers("/web/index.html").permitAll()
                                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()

                                .antMatchers("/api/logout", "/api/login").permitAll()
                                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")
                                .antMatchers("/admin/**").hasAuthority("ADMIN")
                                .antMatchers("/web/accounts.html").hasAuthority("CLIENT");

        // configuracion del formulario de inicio de sesion personalizada
        http.formLogin().usernameParameter("email")
                        .passwordParameter("password")
                        .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        // turn off checking for CSRF (Cross-Site Request Forgery) tokens
        http.csrf().disable();

        // disable frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if USER is NOT AUTHENTICATED send ...
        http.exceptionHandling().authenticationEntryPoint(
                (req,resp,exc) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED) );

        // if LOGIN is SUCCESSFUL, clear the flags using the method 'clearAuthenticationAttributes()' defined below ...
        http.formLogin().successHandler(
                (req, res, auth) -> clearAuthenticationAttributes(req));

        // if LOGIN FAILS, send ...
        http.formLogin().failureHandler(
                (req,res,exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED) );

        // if LOGOUT is SUCCESSFUL, send ...
        http.logout().logoutSuccessHandler( new HttpStatusReturningLogoutSuccessHandler() );

    }


    // method that CLEAR the FLAGS asking for AUTHENTICATION
    private void clearAuthenticationAttributes (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }


}

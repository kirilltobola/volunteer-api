package isu.volunteer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import isu.volunteer.api.jwt.Configurer;
import isu.volunteer.api.jwt.TokenUtil;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private TokenUtil tokenProvider;

    @Autowired
    public WebSecurityConfig(TokenUtil tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/v1/orders/**").hasRole("USER")
            .antMatchers(HttpMethod.PATCH, "/api/v1/orders/**/accept").hasRole("USER")
            .antMatchers("/api/v1/orders/**").hasRole("MEDIC")
            .anyRequest().authenticated()
            .and()
            .apply(new Configurer(this.tokenProvider));
    }
}

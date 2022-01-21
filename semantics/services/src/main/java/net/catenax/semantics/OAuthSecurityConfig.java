package net.catenax.semantics;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .authorizeRequests(auth -> auth
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers("/**/twins/**").authenticated()
            .antMatchers("/**/models/**").authenticated())
          .oauth2ResourceServer()
          .jwt();
    }
}

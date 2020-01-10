package pl.company.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/guinea-pig/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/guinea-pig").permitAll()
                .antMatchers(HttpMethod.GET, "/api/person/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/person").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }
}

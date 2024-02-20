package org.course_planner.gws.config;

//import org.course_planner.gws.filter.CORSFilter;

import org.course_planner.gws.filter.CORSFilter;
import org.course_planner.gws.filter.JwtTokenFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
public class JwtWebSecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtWebSecurityConfig.class);

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private CORSFilter corsFilter;

    @Bean
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable);
        security.httpBasic(AbstractHttpConfigurer::disable);
        security.formLogin(AbstractHttpConfigurer::disable);

        security.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        security.authorizeHttpRequests(request -> {
            request.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
            request.requestMatchers(HttpMethod.POST, "/auth/login").permitAll();
            request.anyRequest().authenticated();
        });

        security.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(corsFilter, CsrfFilter.class);

        return security.build();
    }
}

package com.example.pentaho.cofig;

import com.example.pentaho.component.SecurityConfigComponet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
public class SecurityConfig {


    @Autowired
    private SecurityConfigComponet configuration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return
                http
                        .headers()
                        .contentSecurityPolicy(configuration.getContentSecurityPolicy())
                        .and()
                        .permissionsPolicy().policy(configuration.getPermissionsPolicy())
                        .and()
                        .frameOptions().sameOrigin()
                        .and()
                        .authorizeRequests()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/static/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .and()
                        .formLogin().disable()
                        .cors(Customizer.withDefaults())
                        .csrf()
                        .disable()
                        .build();
    }



    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        List<String> methods = new ArrayList<>(){{
            add("GET");
            add("POST");
        }};

        List<String> headers = new ArrayList<>(){{
            add("Authorization");
            add("Content-Type");
        }};
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(methods);
        corsConfiguration.setAllowedHeaders(headers);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

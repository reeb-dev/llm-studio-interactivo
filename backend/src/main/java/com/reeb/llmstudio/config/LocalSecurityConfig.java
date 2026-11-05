package com.reeb.llmstudio.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Mantiene el laboratorio local abierto; nunca debe usarse con datos reales. */
@Configuration
@ConditionalOnProperty(name = "app.security.enabled", havingValue = "false", matchIfMissing = true)
public class LocalSecurityConfig {
  @Bean
  SecurityFilterChain localLabSecurity(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        .build();
  }
}

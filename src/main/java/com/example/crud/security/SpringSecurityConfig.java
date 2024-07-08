package com.example.crud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.crud.security.filter.JwtAuthenticationFilter;
import com.example.crud.security.filter.JwtValidationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean
  AuthenticationManager authenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
            // .requestMatchers(HttpMethod.POST, "/api/users").hasAnyRole("ADMIN", "USER")
            // .requestMatchers(HttpMethod.GET, "/api/products").hasAnyRole("ADMIN", "USER")
            // .requestMatchers(HttpMethod.GET, "/api/products/{id}").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.POST, "/api/products").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.PUT, "/api/products/{id}").hasRole("ADMIN")
            // .requestMatchers(HttpMethod.DELETE, "/api/products/{id}").hasRole("ADMIN")
            .anyRequest()
            .authenticated())
        .addFilter(new JwtAuthenticationFilter(authenticationManager()))
        .addFilter(new JwtValidationFilter(authenticationManager()))
        .csrf(config -> config.disable())
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

}

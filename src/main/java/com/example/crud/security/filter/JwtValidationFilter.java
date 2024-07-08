package com.example.crud.security.filter;

import static com.example.crud.security.TokenConfig.AUTHORIZATION_HEADER;
import static com.example.crud.security.TokenConfig.CONTENT_TYPE;
import static com.example.crud.security.TokenConfig.PREFIX_TOKEN;
import static com.example.crud.security.TokenConfig.SECRET_KEY;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.example.crud.security.SimpleGrantedAuthorityJsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

  public JwtValidationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String header = request.getHeader(AUTHORIZATION_HEADER);

    if (header == null || !header.startsWith(PREFIX_TOKEN)) {
      chain.doFilter(request, response);
      return;
    }

    String token = header.replace(PREFIX_TOKEN, "");

    try {
      Claims claims = Jwts.parser()
          .verifyWith(SECRET_KEY)
          .build()
          .parseSignedClaims(token)
          .getPayload();

      String username = claims.getSubject();

      String usernameInClaims = (String) claims.get("username");

      Object authoritiesClaims = claims.get("authorities");

      Collection<? extends GrantedAuthority> authorities = Arrays.asList(
          new ObjectMapper()
              .addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
              .readValue(authoritiesClaims.toString(), SimpleGrantedAuthority[].class));

      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
          authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);

      chain.doFilter(request, response);

    } catch (JwtException e) {

      Map<String, Object> body = new HashMap<>();
      body.put("error", e.getMessage());
      body.put("message", "El token JWT es inavalido!");

      response.getWriter().write(new ObjectMapper().writeValueAsString(body));
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType(CONTENT_TYPE);

    }

  }

}

package com.example.crud.security;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenConfig {

  public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

  public static final String PREFIX_TOKEN = "Bearer ";

  public static final String AUTHORIZATION_HEADER = "Authorization";

  public static final String CONTENT_TYPE = "application/json";

  public static final Long EXPIRATION_TIME = 3600000L;

}

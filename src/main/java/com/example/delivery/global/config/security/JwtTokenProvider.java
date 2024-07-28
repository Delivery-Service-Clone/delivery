package com.example.delivery.global.config.security;

import com.example.delivery.domain.user.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 토큰 유효시간 60분

  @Value("${jwt.key}")
  private String key;

  private final CustomUserDetailService customUserDetailService;

  @PostConstruct
  protected void init() {
    key = Base64.getEncoder().encodeToString(key.getBytes(StandardCharsets.UTF_8));
  }

  // JWT 토큰 생성
  public String createToken(String email) {
    Claims claims = Jwts.claims().setSubject(email); // Jwt payload에 저장되는 정보단위
    claims.put("email", email);
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
    return BEARER_PREFIX + Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.ES256, key)
        .compact();
  }

  @Transactional
  public Authentication getAuthentication(String token) {
    UserDetails userDetails = customUserDetailService.loadUserByUsername(
        this.getMemberEmail(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getMemberEmail(String token) {
    return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring("Bearer ".length());
    }
    return null;
  }
}

package com.example.delivery.global.config.security;

import com.example.delivery.domain.user.service.CustomOwnerDetailService;
import com.example.delivery.domain.user.service.CustomUserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
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

  private final CustomUserDetailService customUserDetailService;
  private final CustomOwnerDetailService customOwnerDetailService;

  private static final String BEARER_PREFIX = "Bearer ";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 토큰 유효시간 60분

  @Value("${jwt.secret}")
  private String secretKey;

  private Key key;

  // application.yml에서 secret 값 가져와서 key에 저장하는 초기화 메서드
  @PostConstruct
  protected void init() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  // JWT 토큰 생성
  public String createToken(String email, String userType) {
    Claims claims = Jwts.claims().setSubject(email); // Jwt payload에 저장되는 정보단위
    claims.put("email", email);
    claims.put("userType", userType);
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);
    return BEARER_PREFIX
        + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256) // HMAC 알고리즘 사용
            .compact();
  }

  @Transactional
  public Authentication getAuthentication(String token) {
    String userType = getUserTypeFromToken(token);
    UserDetails userDetails;

    if ("OWNER".equals(userType)) {
      userDetails = customOwnerDetailService.loadUserByUsername(getMemberEmail(token));
    } else {
      userDetails = customUserDetailService.loadUserByUsername(getMemberEmail(token));
    }

    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  private String getMemberEmail(String token) {
    return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
  }

  private String getUserTypeFromToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    return claims.get("userType", String.class);
  }

  public boolean validateToken(String jwtToken) {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwtToken);
      return !claims.getBody().getExpiration().before(new Date());
    } catch (SignatureException e) {
      throw e; // 서명 예외를 다시 던집니다.
    } catch (MalformedJwtException e) {
      throw e; // 잘못된 JWT 예외를 다시 던집니다.
    } catch (ExpiredJwtException e) {
      throw e; // 만료된 JWT 예외를 다시 던집니다.
    } catch (Exception e) {
      throw new RuntimeException("토큰 검증 중 오류 발생", e); // 일반 예외를 다시 던집니다.
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

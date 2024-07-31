package com.example.delivery.global.config.security;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    try {
      String token = jwtTokenProvider.resolveToken(request);
      if (token != null && jwtTokenProvider.validateToken(token)) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        // SecurityContext 에 Authentication 객체 저장
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
      chain.doFilter(request, response);
    } catch (Exception e) {
      ErrorResponse errorResponse = null;
      log.error("error= ", e);

      if (!response.isCommitted()) {
        if (e instanceof ExpiredJwtException) {
          errorResponse = ErrorResponse.of(ErrorCode.JWT_EXPIRED);
        } else if (e instanceof SignatureException) {
          errorResponse = ErrorResponse.of(ErrorCode.JWT_INVALID);
        } else if (e instanceof MalformedJwtException) {
          errorResponse = ErrorResponse.of(ErrorCode.JWT_INVALID_FORMAT);
        } else {
          errorResponse = ErrorResponse.of(ErrorCode.JWT_INVALID);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(json);
      }
    }
  }
}

package com.example.delivery.global.config.security;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.JWT_MISSING);
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.setCharacterEncoding("utf-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
    response.getWriter().write(jsonErrorResponse);
  }
}

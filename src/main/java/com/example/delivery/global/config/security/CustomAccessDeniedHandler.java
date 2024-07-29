package com.example.delivery.global.config.security;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.ACCESS_DENIED);
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonErrorResponse = objectMapper.writeValueAsString(errorResponse);

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setCharacterEncoding("utf-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
    response.getWriter().write(jsonErrorResponse);
  }
}

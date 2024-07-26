package com.example.delivery.global.handler;

import static com.example.delivery.global.error.ResponseEntity.failureResponse;

import com.example.delivery.global.error.ResponseEntity;
import com.example.delivery.global.error.ResponseType;
import com.example.delivery.global.exception.BaseException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // controller의 exception catch
public class GlobalExceptionHandler {

  @ExceptionHandler(value = BaseException.class)
  protected ResponseEntity handleException(BaseException e, HttpServletResponse response) {
    response.setStatus(e.getResponseType().getStatus());
    StackTraceElement ste = e.getStackTrace()[0];
    log.error(
        "[{}-{}] : {}", ste.getClassName(), ste.getLineNumber(), e.getResponseType().getMessage());
    return failureResponse(e.getResponseType());
  }

  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  protected ResponseEntity handleUnexpectedException(Exception e) {
    log.error(e.getMessage(), e);
    StackTraceElement ste = e.getStackTrace()[0];
    log.error("[{}-{}] : {}", ste.getClassName(), ste.getLineNumber(), e.getMessage());
    return failureResponse(ResponseType.FAILURE);
  }

  //  @ExceptionHandler(AuthenticationException.class)
  //  public ResponseEntity handleAuthentication(
  //      AuthenticationException e, HttpServletResponse response) {
  //    if (e instanceof UsernameNotFoundException) {
  //      response.setStatus(ResponseType.USER_NOT_FOUND.getStatus());
  //      return failureResponse(ResponseType.USER_NOT_FOUND);
  //    } else if (e instanceof BadCredentialsException) {
  //      response.setStatus(ResponseType.INVALID_USER_PASSWORD.getStatus());
  //      return failureResponse(ResponseType.INVALID_USER_PASSWORD);
  //    } else if (e instanceof AccountExpiredException) {
  //      response.setStatus(ResponseType.USER_ACCOUNT_EXPIRED.getStatus());
  //      return failureResponse(ResponseType.USER_ACCOUNT_EXPIRED);
  //    } else if (e instanceof CredentialsExpiredException) {
  //      response.setStatus(ResponseType.USER_PASSWORD_EXPIRED.getStatus());
  //      return failureResponse(ResponseType.USER_PASSWORD_EXPIRED);
  //    } else if (e instanceof LockedException) {
  //      response.setStatus(ResponseType.USER_ACCOUNT_LOCKED.getStatus());
  //      return failureResponse(ResponseType.USER_ACCOUNT_LOCKED);
  //    } else if (e instanceof DisabledException) {
  //      response.setStatus(ResponseType.USER_ACCOUNT_DISABLED.getStatus());
  //      return failureResponse(ResponseType.USER_ACCOUNT_DISABLED);
  //    } else if (e instanceof InsufficientAuthenticationException) {
  //      response.setStatus(ResponseType.UNAUTHORIZED_REQUEST.getStatus());
  //      return failureResponse(ResponseType.UNAUTHORIZED_REQUEST);
  //    } else {
  //      return failureResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()), e.getMessage());
  //    }
  //  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    return failureResponse(
        String.valueOf(HttpStatus.BAD_REQUEST.value()),
        e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
  }
}

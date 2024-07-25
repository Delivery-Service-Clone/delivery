package com.example.delivery.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.LOCKED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.PAYLOAD_TOO_LARGE;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseType {

  // COMMON
  SUCCESS(200, "CM00", "success :)"),
  FAILURE(500, "CM99", "예상하지 못한 에러가 발생하였습니다."),

  // Internal Server Error
  INVALID_ARGUMENT(UNAUTHORIZED.value(), "CM01", "Argument 유효성 검증에 실패하였습니다."),
  INVALID_REQUEST(BAD_REQUEST.value(), "CM02", "유효하지 않는 요청입니다."),
  UNAUTHORIZED_REQUEST(UNAUTHORIZED.value(), "CM03", "비인증된 요청입니다."),
  DATA_DUPLICATED(BAD_REQUEST.value(), "CM04", "이미 존재하는 값입니다"),
  INVALID_FORMAT(BAD_REQUEST.value(), "CM05", "입력 형식이 올바르지 않습니다."),

  // JWT
  JWT_NOT_VALID(UNAUTHORIZED.value(), "JW01", "올바르지 않은 토큰입니다."),
  JWT_MALFORMED(UNAUTHORIZED.value(), "JW02", "토큰 구조가 잘못되었습니다."),
  JWT_UNSUPPORTED(UNAUTHORIZED.value(), "JW03", "지원하지 않는 토큰입니다."),
  JWT_SIGNATURE(UNAUTHORIZED.value(), "JW04", "서명 검증에 실패한 토큰입니다."),
  JWT_EXPIRED(UNAUTHORIZED.value(), "JW05", "만료된 토큰입니다."),
  JWT_NULL_OR_EMPTY(UNAUTHORIZED.value(), "JW06", "토큰이 없거나 값이 비어있습니다."),
  JWT_PREMATURE(UNAUTHORIZED.value(), "JW07", "토큰이 활성화되지 않았습니다."),
  JWT_NOT_FOUND(NOT_FOUND.value(), "JW08", "토큰을 찾을 수 없습니다."),
  UNMATCHED_SAVED_JWT(UNAUTHORIZED.value(), "JW09", "저장된 토큰과 일치하지 않습니다."),
  UNMATCHED_JWT(UNAUTHORIZED.value(), "JW10", "Access 와 Refresh 토큰의 사용자가 일치하지 않습니다."),

  // AUTH
  DUPLICATED_EMAIL(BAD_REQUEST.value(), "AU01", "이미 사용 중인 이메일입니다."),
  DUPLICATED_NICKNAME(BAD_REQUEST.value(), "AU02", "이미 사용 중인 닉네임입니다."),
  UNMATCHED_PROVIDER(UNAUTHORIZED.value(), "AU03", "가입된 OAuth 제공자와 일치하지 않습니다."),
  RE_LOGIN_REQUIRED(UNAUTHORIZED.value(), "AU04", "재로그인이 필요합니다."),
  UNSUPPORTED_PROVIDER(BAD_REQUEST.value(), "AU05", "지원하지 않는 소셜 로그인입니다"),
  OAUTH2_USER_NOT_FOUND(NOT_FOUND.value(), "AU06", "OAuth2 provider의 email이 존재하지 않습니다"),
  INVALID_REDIRECT_URL(UNAUTHORIZED.value(), "AU07", "redirect url이 유효하지 않습니다"),
  REDIRECT_URL_NOT_FOUND(BAD_REQUEST.value(), "AU08", "요청에 redirect url이 포함되지 않았습니다."),

  // USER
  USER_NOT_FOUND(UNAUTHORIZED.value(), "US01", "유저를 찾을 수 없습니다."),
  USER_ACCOUNT_EXPIRED(UNAUTHORIZED.value(), "US02", "유저 계정이 만료되었습니다."),
  USER_ACCOUNT_LOCKED(LOCKED.value(), "US03", "유저 계정이 잠겨있습니다."),
  USER_ACCOUNT_DISABLED(UNAUTHORIZED.value(), "US04", "유저 계정이 비활성화인 상태입니다."),
  USER_PASSWORD_EXPIRED(UNAUTHORIZED.value(), "US05", "유저 패스워드가 만료되었습니다."),
  INVALID_USER_PASSWORD(UNAUTHORIZED.value(), "US06", "유저 패스워드가 일치하지 않습니다."),

  // POST
  POST_NOT_FOUND(NOT_FOUND.value(), "PO01", "해당 게시글을 찾을 수 없습니다."),
  STATUS_NOT_FOUND(NOT_FOUND.value(), "PO02", "상태값을 찾을 수 없습니다."),
  UNMATCHED_POST_AND_USER(UNAUTHORIZED.value(), "P003", "해당 유저가 작성한 게시물이 아닙니다."),
  INVALID_POST_SEARCH_TYPE(UNAUTHORIZED.value(), "P004", "게시물 검색 타입이 잘못되었습니다."),

  // COMMENT
  COMMENT_NOT_FOUND(NOT_FOUND.value(), "C001", "해당 댓글을 찾을 수 없습니다."),
  UNMATCHED_COMMENT_AND_USER(UNAUTHORIZED.value(), "C002", "해당 유저가 작성한 댓글이 아닙니다."),

  // ABUSE REPORT
  COMMENT_ABUSE_REPORT_NOT_FOUND(NOT_FOUND.value(), "R001", "해당 댓글 신고건을 찾을 수 없습니다."),
  POST_ABUSE_REPORT_NOT_FOUND(NOT_FOUND.value(), "R002", "해당 게시글 신고건을 찾을 수 없습니다."),

  // IMAGE
  UNSUPPORTED_FILE_TYPE(UNSUPPORTED_MEDIA_TYPE.value(), "I001", "해당 파일은 지원하는 파일 형식이 아닙니다."),
  FILE_NOT_UPLOAD(BAD_REQUEST.value(), "I002", "이미지를 업로드하지 않았습니다."),
  MAX_UPLOAD_SIZE_EXCEEDED(PAYLOAD_TOO_LARGE.value(), "I003", "이미지 용량이 초과되었습니다.");

  private final int status;
  private final String code;
  private final String message;
}

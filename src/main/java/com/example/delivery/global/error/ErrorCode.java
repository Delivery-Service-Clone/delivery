package com.example.delivery.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** {주체}_{이유} message 는 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ErrorCode {
  // Global
  INTERNAL_SERVER_ERROR(500, "G001", "서버 오류"),
  INPUT_INVALID_VALUE(409, "G002", "잘못된 입력"),

  // User 도메인
  INVALID_PASSWORD(400, "U001", "잘못된 비밀번호"),
  USER_NOT_FOUND_ERROR(400, "U002", "사용자를 찾을 수 없음"),
  INVALID_USER_PASSWORD(400, "U003", "비밀번호가 일치하지 않음"),
  ALREADY_EXIST_USER(400, "U004", "이미 가입된 이메일이 존재합니다."),
  INVALID_AUTH_TOKEN(401, "U005", "권한 정보가 없는 토큰입니다."),

  ALREADY_EXIST_USER_BY_NICKNAME(400, "U006", "이미 가입된 유저 nickname 입니다."),
  NOT_FOUND_SEARCH_KEYWORD(400, "S006", "해당 검색어를 삭제할 수 없습니다."),

  // Owner
  OWNER_NOT_FOUND_ERROR(400, "O001", "가게 주인을 찾을 수 없음");

  private final int status;
  private final String code;
  private final String message;
}
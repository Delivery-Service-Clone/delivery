package com.example.delivery.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** {행위}_{목적어}_{성공여부} message 는 동사 명사형으로 마무리 */
@Getter
@AllArgsConstructor
public enum ResultCode {

  // 도메인 별로 나눠서 관리(ex: User 도메인)
  // user
  USER_REGISTRATION_SUCCESS("U001", "200", "사용자가 정상적으로 등록되었습니다."),
  GET_ALL_USER_SUCCESS("U002", "200", "모든 사용자를 정상적으로 불러왔습니다."),
  GET_USER_SUCCESS("U003", "200", "지정 사용자를 정상적으로 불러왔습니다."),
  DELETE_USER_SUCCESS("U004", "200", "지정 사용자를 정상적으로 삭제했습니다."),
  LOGIN_SUCCESS("U005", "200", "정상적으로 로그인 되었습니다."),
  CHECK_EMAIL_GOOD("U006", "200", "사용 가능한 이메일입니다."),
  CHECK_EMAIL_BAD("U007", "200", "사용 불가능한 이메일입니다."),

  // store
  STORE_REGISTRATION_SUCCESS("S001", "200", "식당이 정상적으로 등록되었습니다."),
  CHANGE_STORESTATUS_SUCCESS("S002", "200", "식당 상태가 정상적으로 변경되었습니다."),
  GET_STORES_SUCCESS("S003", "200", "모든 식장을 정상적으로 불러왔습니다."),
  // menu
  MENU_REGISTRATION_SUCCESS("M001", "200", "메뉴가 정상적으로 등록되었습니다."),
  MENU_GET_SUCCESS("M002", "200", "메뉴가 정상적으로 불러왔습니다."),

  //rider
  RIDER_REGISTRATION_SUCCESS("R001", "200", "라이더가 정상적으로 등록되었습니다."),

  //FCM
  FCM_SEND_SUCCESS("F001", "200", "메시지가 정상적으로 전송되었습니다."),
  FCM_SEND_FAIL("F001", "400", "메시지 전송이 실패했습니다"),
  ;

  private final String code;
  private final String status;
  private final String message;
}

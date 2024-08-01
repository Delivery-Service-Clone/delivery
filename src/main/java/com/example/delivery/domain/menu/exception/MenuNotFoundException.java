package com.example.delivery.domain.menu.exception;

import com.example.delivery.global.error.ErrorCode;
import com.example.delivery.global.error.exception.BusinessException;

public class MenuNotFoundException extends BusinessException {

  public MenuNotFoundException() {
    super(ErrorCode.MENU_NOT_FOUND_ERROR);
  }
}

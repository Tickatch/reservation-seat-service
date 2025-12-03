package com.tickatch.reservationseatservice.reservationseat.domain.exception;

import io.github.tickatch.common.error.BusinessException;
import io.github.tickatch.common.error.ErrorCode;

public class ReservationSeatException extends BusinessException {
  public ReservationSeatException(ErrorCode errorCode) {
    super(errorCode);
  }
}

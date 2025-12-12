package com.tickatch.reservationseatservice.reservationseat.domain.exception;

import io.github.tickatch.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 예매 좌석 관련 오류 코드.
 *
 * <p>예매 좌석 처리 과정에서 발생할 수 있는 예외 상황을 정의한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RequiredArgsConstructor
public enum ReservationSeatErrorCode implements ErrorCode {
  RESERVATION_SEAT_UNAVAILABLE(HttpStatus.CONFLICT.value(), "RESERVATION_SEAT_UNAVAILABLE"),
  RESERVATION_SEAT_ALREADY_AVAILABLE(
      HttpStatus.BAD_REQUEST.value(), "RESERVATION_SEAT_ALREADY_AVAILABLE"),
  RESERVATION_SEAT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "RESERVATION_SEAT_NOT_FOUND"),
  ;

  private final int status;
  private final String code;

  @Override
  public int getStatus() {
    return this.status;
  }

  @Override
  public String getCode() {
    return this.code;
  }
}

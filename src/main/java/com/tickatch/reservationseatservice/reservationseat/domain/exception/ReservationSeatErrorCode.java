package com.tickatch.reservationseatservice.reservationseat.domain.exception;

import io.github.tickatch.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationSeatErrorCode implements ErrorCode {
  RESERVATION_SEAT_UNAVAILABLE(HttpStatus.CONFLICT.value(), "RESERVATION_SEAT_UNAVAILABLE"),
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

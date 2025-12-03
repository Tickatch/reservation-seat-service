package com.tickatch.reservationseatservice.reservationseat.domain;

import io.github.tickatch.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ReservationSeatErrorCode implements ErrorCode {
  SEAT_UNAVAILABLE(HttpStatus.CONFLICT.value(), "SEAT_UNAVAILABLE"),
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

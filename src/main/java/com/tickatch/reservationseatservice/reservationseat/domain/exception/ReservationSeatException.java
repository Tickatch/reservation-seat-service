package com.tickatch.reservationseatservice.reservationseat.domain.exception;

import io.github.tickatch.common.error.BusinessException;
import io.github.tickatch.common.error.ErrorCode;

/**
 * 예매 좌석 처리 예외.
 *
 * <p>예매 좌석 도메인에서 발생하는 비즈니스 예외를 표현한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public class ReservationSeatException extends BusinessException {

  /**
   * 예매 좌석 예외 생성자.
   *
   * @param errorCode 예매 좌석 예외 코드
   */
  public ReservationSeatException(ErrorCode errorCode) {
    super(errorCode);
  }

  /**
   * 예매 좌석 예외 생성자.
   *
   * @param errorCode 예매 좌석 예외 코드
   * @param errorArgs 오류 메시지에 바인딩될 인자 값
   */
  public ReservationSeatException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }

  public ReservationSeatException(ErrorCode errorCode, Throwable throwable) {
    super(errorCode, throwable);
  }
}

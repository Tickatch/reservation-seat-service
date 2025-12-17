package com.tickatch.reservationseatservice.reservationseat.domain;

/**
 * 예매 좌석 상태를 나타내는 열거형.
 *
 * <p>예매 좌석이 가질 수 있는 상태를 정의하며, 좌석의 사용 가능 여부 판단 기능을 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public enum ReservationSeatStatus {
  AVAILABLE,
  PREEMPT,
  RESERVED;

  /**
   * 예매 좌석이 사용 불가능한 상태인지 여부를 반환한다.
   *
   * @return 사용 불가능하면 {@code true}, 그렇지 않으면 {@code false}
   */
  public boolean isUnavailable() {
    return !isAvailable();
  }

  public boolean isPreempt() {
    return this == PREEMPT;
  }

  /**
   * 예매 좌석이 사용 가능한 상태인지 여부를 반환한다.
   *
   * @return 사용 가능하면 {@code true}, 그렇지 않으면 {@code false}
   */
  public boolean isAvailable() {
    return this == AVAILABLE;
  }
}

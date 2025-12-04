package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatInfosUpdateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * 예매 좌석 관리 기능 제공 인터페이스.
 *
 * <p>예매 좌석의 정보 수정, 선점, 예약, 취소 및 삭제 기능을 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface ReservationSeatManager {

  /**
   * 예매 좌석 정보 수정.
   *
   * <p>전달된 요청 정보를 기반으로 좌석 등급 및 가격 정보를 수정한다.
   *
   * @param updateRequest 예매 좌석 정보 수정 요청
   */
  void updateReservationSeatInfo(@Valid ReservationSeatInfosUpdateRequest updateRequest);

  /**
   * 예매 좌석 선점.
   *
   * <p>해당 예매 좌석을 사용자가 잠시 확보할 수 있도록 상태를 변경한다.
   *
   * @param reservationSeatId 예매 좌석 ID
   */
  void preempt(@NotNull Long reservationSeatId);

  /**
   * 예매 좌석 예약 확정.
   *
   * <p>선점된 예매 좌석을 확정 상태로 변경한다.
   *
   * @param reservationSeatId 예매 좌석 ID
   */
  void reserve(@NotNull Long reservationSeatId);

  /**
   * 예매 좌석 예약 취소.
   *
   * <p>예약 또는 선점 상태의 예매 좌석을 취소하여 다시 예약 가능 상태로 변경한다.
   *
   * @param reservationSeatId 예매 좌석 ID
   */
  void cancel(@NotNull Long reservationSeatId);

  /**
   * 상품별 예매 좌석 삭제.
   *
   * <p>해당 상품에 포함된 모든 예매 좌석을 삭제한다.
   *
   * @param productId 상품 ID
   */
  void delete(@NotNull Long productId);
}

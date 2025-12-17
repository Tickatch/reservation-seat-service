package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import java.util.List;

/**
 * 예매 좌석 조회 기능 제공 인터페이스.
 *
 * <p>예매 좌석을 <b>ID</b> 또는 <b>상품 ID</b> 기준으로 조회하며, 동시성 처리를 위해 잠금 조회 기능을 지원한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface ReservationSeatFinder {

  /**
   * 예매 좌석 단건 조회.
   *
   * <p>예매 좌석 <b>ID</b>를 기준으로 좌석 정보를 조회한다.
   *
   * @param id 예매 좌석 ID
   * @return 조회된 예매 좌석
   */
  ReservationSeat findById(Long id);

  /**
   * 예매 좌석 단건 조회(<span style="color:red;">LOCK</span>).
   *
   * <p>예매 좌석 <b>ID</b>를 기준으로 좌석 정보를 조회하며, 동시성 제어를 위해 조회 시 데이터베이스 잠금을 적용한다.
   *
   * @param id 예매 좌석 ID
   * @return 조회된 예매 좌석
   */
  ReservationSeat findByIdWithLock(Long id);

  /**
   * 상품별 예매 좌석 목록 조회.
   *
   * <p>지정된 <b>상품 ID</b>에 속한 모든 예매 좌석을 조회한다.
   *
   * @param productId 상품 ID
   * @return 상품에 포함된 예매 좌석 목록
   */
  List<ReservationSeat> findAllBy(Long productId);
}

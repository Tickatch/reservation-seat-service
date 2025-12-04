package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 예매 좌석 생성 기능 제공 인터페이스.
 *
 * <p>예매 좌석 생성 요청 정보를 기반으로 여러 개의 좌석을 등록한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface ReservationSeatCreator {

  /**
   * 예매 좌석 생성.
   *
   * <p>전달된 요청 정보를 기반으로 다수의 예매 좌석을 생성하고 저장한다.
   *
   * @param createRequest 예매 좌석 생성 요청 정보
   * @return 생성된 예매 좌석 목록
   */
  List<ReservationSeat> create(@Valid ReservationSeatsCreateRequest createRequest);
}

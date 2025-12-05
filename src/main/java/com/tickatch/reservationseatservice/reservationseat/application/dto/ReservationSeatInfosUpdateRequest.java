package com.tickatch.reservationseatservice.reservationseat.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 예매 좌석 정보 수정 요청.
 *
 * <p>여러 예매 좌석의 등급 및 가격 정보를 일괄 수정하기 위한 요청 객체이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ReservationSeatInfosUpdateRequest(
    @NotNull @Valid List<ReservationSeatInfoUpdateRequest> seatInfos) {

  /** 생성자 내부에서 입력된 좌석 정보 목록을 <code>immutable</code> 리스트로 변환한다. */
  public ReservationSeatInfosUpdateRequest {
    seatInfos = List.copyOf(seatInfos);
  }

  /**
   * 수정 대상 예매 좌석 ID 목록을 반환한다.
   *
   * <p>요청에 포함된 각 예매 좌석 정보 객체에서 ID만 추출하여 제공한다.
   *
   * @return 예매 좌석 ID 목록
   */
  @JsonIgnore
  public List<Long> getReservationSeatIds() {
    return seatInfos.stream().map(ReservationSeatInfoUpdateRequest::reservationSeatId).toList();
  }
}

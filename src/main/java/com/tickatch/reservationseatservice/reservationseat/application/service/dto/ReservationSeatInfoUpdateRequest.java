package com.tickatch.reservationseatservice.reservationseat.application.service.dto;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.SeatInfoUpdateRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 예매 좌석 정보 수정 요청.
 *
 * <p>특정 예매 좌석의 등급, 가격 정보를 수정하기 위한 요청 객체이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ReservationSeatInfoUpdateRequest(
    @NotNull Long reservationSeatId, @NotBlank String grade, @NotNull @Min(0) Long price) {

  /**
   * 예매 좌석 정보 수정을 위한 내부 도메인 요청 객체로 변환한다.
   *
   * @return 좌석 정보 수정 요청 객체
   */
  public SeatInfoUpdateRequest toUpdateRequest() {
    return new SeatInfoUpdateRequest(grade, price);
  }
}

package com.tickatch.reservationseatservice.reservationseat.application.dto;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 예매 좌석 생성 요청.
 *
 * <p>상품에 대한 여러 예매 좌석을 일괄 생성하기 위한 요청 객체이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ReservationSeatsCreateRequest(
    @NotNull Long productId, @NotNull List<ReservationSeatCreateInfo> seatCreateInfos) {

  /** 생성자 내부에서 입력된 좌석 정보 목록을 <code>immutable</code> 리스트로 변환한다. */
  public ReservationSeatsCreateRequest {
    seatCreateInfos = List.copyOf(seatCreateInfos);
  }

  /**
   * 예매 좌석 생성 도메인 요청 객체 목록으로 변환한다.
   *
   * @return 예매 좌석 생성 요청 객체 목록
   */
  public List<ReservationSeatCreateRequest> toCreateRequests() {
    return this.seatCreateInfos.stream()
        .map(
            seatCreateInfo ->
                new ReservationSeatCreateRequest(
                    productId,
                    seatCreateInfo.seatNumber(),
                    seatCreateInfo.grade(),
                    seatCreateInfo.price()))
        .toList();
  }
}

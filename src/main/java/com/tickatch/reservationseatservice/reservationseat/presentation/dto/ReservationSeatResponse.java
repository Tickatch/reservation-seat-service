package com.tickatch.reservationseatservice.reservationseat.presentation.dto;

import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.SeatInfo;

/**
 * 예매 좌석 정보를 응답하기 위한 DTO.
 *
 * <p>예매 좌석의 식별자, 좌석 번호, 등급, 가격 정보를 포함한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ReservationSeatResponse(Long id, String seatNumber, String grade, Long price) {

  /**
   * 예매 좌석 도메인 객체를 응답 객체로 변환한다.
   *
   * @param reservationSeat 변환할 예매 좌석 도메인 객체
   * @return 변환된 예매 좌석 응답 DTO
   */
  public static ReservationSeatResponse from(ReservationSeat reservationSeat) {
    SeatInfo seatInfo = reservationSeat.getSeatInfo();
    return new ReservationSeatResponse(
        reservationSeat.getId(),
        seatInfo.getSeatNumber(),
        seatInfo.getGrade(),
        seatInfo.getPrice().value());
  }
}

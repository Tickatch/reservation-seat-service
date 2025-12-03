package com.tickatch.reservationseatservice.reservationseat.presentation.api.dto;

import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.SeatInfo;

public record ReservationSeatResponse(Long id, String seatNumber, String grade, Long price) {
  public static ReservationSeatResponse from(ReservationSeat reservationSeat) {
    SeatInfo seatInfo = reservationSeat.getSeatInfo();
    return new ReservationSeatResponse(
        reservationSeat.getId(),
        seatInfo.getSeatNumber(),
        seatInfo.getGrade(),
        seatInfo.getPrice().value());
  }
}

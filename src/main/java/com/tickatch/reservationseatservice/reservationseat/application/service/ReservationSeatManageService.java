package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatRepository;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationSeatManageService implements ReservationSeatManager {

  private final ReservationSeatRepository reservationSeatRepository;

  @Override
  public void updateReservationSeatInfo(ReservationSeatInfosUpdateRequest updateRequest) {
    List<Long> reservationSeatIds = updateRequest.getReservationSeatIds();

    List<ReservationSeat> reservationSeats =
        reservationSeatRepository.findAllById(reservationSeatIds);
    Map<Long, ReservationSeat> reservationSeatMap =
        reservationSeats.stream()
            .collect(Collectors.toMap(ReservationSeat::getId, Function.identity()));

    updateRequest
        .seatInfos()
        .forEach(
            updateInfo -> {
              ReservationSeat seat = reservationSeatMap.get(updateInfo.reservationSeatId());
              seat.updateSeatInfo(updateInfo.toUpdateRequest());
            });

    reservationSeatRepository.saveAll(reservationSeats);
  }
}

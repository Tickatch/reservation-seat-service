package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatRepository;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReservationSeatCreateService implements ReservationSeatCreator {

  private final ReservationSeatRepository reservationSeatRepository;

  @Override
  public List<ReservationSeat> create(ReservationSeatsCreateRequest createRequest) {
    List<ReservationSeatCreateRequest> createRequests = createRequest.toCreateRequests();

    List<ReservationSeat> reservationSeats =
        createRequests.stream().map(ReservationSeat::create).toList();

    return reservationSeatRepository.saveAll(reservationSeats);
  }
}

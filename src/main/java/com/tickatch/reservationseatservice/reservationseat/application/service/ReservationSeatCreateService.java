package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatRepository;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 예매 좌석 생성 서비스.
 *
 * <p>예매 좌석 생성 요청을 처리하여 다수의 예매 좌석을 등록하는 서비스 구현체이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
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

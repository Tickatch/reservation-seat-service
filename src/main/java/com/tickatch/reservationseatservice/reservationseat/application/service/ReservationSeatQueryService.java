package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatRepository;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatErrorCode;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ReservationSeatQueryService implements ReservationSeatFinder {
  private final ReservationSeatRepository reservationSeatRepository;

  @Override
  public ReservationSeat findById(Long id) {
    return reservationSeatRepository
        .findById(id)
        .orElseThrow(
            () ->
                new ReservationSeatException(
                    ReservationSeatErrorCode.RESERVATION_SEAT_NOT_FOUND, id));
  }

  @Override
  public ReservationSeat findByIdWithLock(Long id) {
    return reservationSeatRepository
        .findByIdWithLock(id)
        .orElseThrow(
            () ->
                new ReservationSeatException(
                    ReservationSeatErrorCode.RESERVATION_SEAT_NOT_FOUND, id));
  }

  @Override
  public List<ReservationSeat> findAllBy(Long productId) {
    return reservationSeatRepository.findAllByProductId(ProductId.of(productId));
  }
}

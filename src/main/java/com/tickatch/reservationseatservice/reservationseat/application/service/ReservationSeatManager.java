package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfosUpdateRequest;
import jakarta.validation.Valid;

public interface ReservationSeatManager {
  void updateReservationSeatInfo(@Valid ReservationSeatInfosUpdateRequest updateRequest);

  void preempt(@Valid Long reservationSeatId);

  void reserve(@Valid Long reservationSeatId);

  void cancel(@Valid Long reservationSeatId);

  void delete(Long productId);
}

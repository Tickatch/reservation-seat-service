package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;

public interface ReservationSeatFinder {
  ReservationSeat findById(Long id);

  ReservationSeat findByIdWithLock(Long id);
}

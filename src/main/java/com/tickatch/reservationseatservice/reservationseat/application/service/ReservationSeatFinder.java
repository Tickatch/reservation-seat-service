package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import java.util.List;

public interface ReservationSeatFinder {
  ReservationSeat findById(Long id);

  ReservationSeat findByIdWithLock(Long id);

  List<ReservationSeat> findAllBy(Long productId);
}

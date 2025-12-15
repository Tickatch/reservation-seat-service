package com.tickatch.reservationseatservice.reservationseat.application;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCanceledEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatPreemptEvent;

public interface ReservationSeatEventPublisher {
  void publishPreempt(ReservationSeatPreemptEvent preemptEvent);

  void publishCanceled(ReservationSeatCanceledEvent reservationSeat);
}

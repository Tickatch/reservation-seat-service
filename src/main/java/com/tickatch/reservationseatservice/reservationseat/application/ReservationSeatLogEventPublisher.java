package com.tickatch.reservationseatservice.reservationseat.application;

public interface ReservationSeatLogEventPublisher {
  void publish(Long reservationSeatId, String seatNumber, String actionType);
}

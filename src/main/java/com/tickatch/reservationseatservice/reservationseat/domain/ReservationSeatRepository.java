package com.tickatch.reservationseatservice.reservationseat.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ReservationSeatRepository extends Repository<ReservationSeat, Long> {
  ReservationSeat save(ReservationSeat reservationSeat);

  <S extends ReservationSeat> List<S> saveAll(Iterable<S> reservationSeats);

  Optional<ReservationSeat> findById(Long id);

  List<ReservationSeat> findAllById(List<Long> productId);
}

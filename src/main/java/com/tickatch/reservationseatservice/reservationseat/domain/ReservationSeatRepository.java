package com.tickatch.reservationseatservice.reservationseat.domain;

import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface ReservationSeatRepository extends Repository<ReservationSeat, Long> {
  ReservationSeat save(ReservationSeat reservationSeat);

  <S extends ReservationSeat> List<S> saveAll(Iterable<S> reservationSeats);

  Optional<ReservationSeat> findById(Long id);

  List<ReservationSeat> findAllByIdIn(List<Long> ids);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT rs FROM ReservationSeat rs WHERE rs.id = :id")
  Optional<ReservationSeat> findByIdWithLock(Long id);

  List<ReservationSeat> findAllByProductId(ProductId productId);

  void deleteAllByProductId(ProductId productId);
}

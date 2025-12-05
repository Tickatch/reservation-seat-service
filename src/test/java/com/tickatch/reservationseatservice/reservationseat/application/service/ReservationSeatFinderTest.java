package com.tickatch.reservationseatservice.reservationseat.application.service;

import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tickatch.reservationseatservice.PersistenceTest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@PersistenceTest
@RequiredArgsConstructor
class ReservationSeatFinderTest {
  private final ReservationSeatCreator reservationSeatCreator;
  private final ReservationSeatFinder reservationSeatFinder;
  private final EntityManager em;

  Long productId = 1L;

  @Test
  void findAllBy() {
    createReservationSeats(2L);
    List<ReservationSeat> reservationSeats = createReservationSeats(productId);

    List<ReservationSeat> findSeats = reservationSeatFinder.findAllBy(productId);

    assertThat(findSeats).containsExactlyInAnyOrderElementsOf(reservationSeats);
  }

  private List<ReservationSeat> createReservationSeats(Long productId) {
    var createRequest = createReservationSeatsCreateRequest(productId);
    List<ReservationSeat> seats = reservationSeatCreator.create(createRequest);

    em.flush();
    em.clear();

    return seats;
  }
}

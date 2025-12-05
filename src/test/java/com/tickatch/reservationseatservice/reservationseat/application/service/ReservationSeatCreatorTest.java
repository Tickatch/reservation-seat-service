package com.tickatch.reservationseatservice.reservationseat.application.service;

import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.createReservationSeatsCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import com.tickatch.reservationseatservice.PersistenceTest;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;

@PersistenceTest
record ReservationSeatCreatorTest(ReservationSeatCreator reservationSeatCreator, EntityManager em) {
  @Test
  void create() {
    ReservationSeatsCreateRequest request = createReservationSeatsCreateRequest();

    List<ReservationSeat> reservationSeats = reservationSeatCreator.create(request);

    assertThat(reservationSeats).hasSize(3);
    assertThat(reservationSeats)
        .anySatisfy(
            reservationSeat -> {
              assertThat(reservationSeat.getId()).isNotNull();
              assertThat(reservationSeat.getCreatedAt()).isNotNull();
            });
  }
}

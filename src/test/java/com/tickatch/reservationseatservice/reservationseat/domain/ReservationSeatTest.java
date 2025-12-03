package com.tickatch.reservationseatservice.reservationseat.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReservationSeatTest {
  @Test
  void create() {
    ReservationSeatCreateRequest request =
        new ReservationSeatCreateRequest(UUID.randomUUID(), "A1", "S석", 10000L);

    ReservationSeat seat = ReservationSeat.create(request);

    assertThat(seat.getProductId()).isEqualTo(ProductId.of(request.productId()));
    assertThat(seat.getSeatInfo())
        .isEqualTo(SeatInfo.of(request.seatNumber(), request.grade(), Price.of(request.price())));
    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.AVAILABLE);
  }
}

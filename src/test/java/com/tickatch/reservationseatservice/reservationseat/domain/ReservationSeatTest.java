package com.tickatch.reservationseatservice.reservationseat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationSeatTest {

  private ReservationSeat seat;

  @BeforeEach
  void setUp() {
    ReservationSeatCreateRequest request =
        new ReservationSeatCreateRequest(UUID.randomUUID(), "A1", "S석", 10000L);

    seat = ReservationSeat.create(request);
  }

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

  @Test
  void updateSeatInfo() {
    SeatInfoUpdateRequest request = new SeatInfoUpdateRequest("A석", 8000L);

    seat.updateSeatInfo(request);

    assertThat(seat.getSeatInfo().getGrade()).isEqualTo("A석");
    assertThat(seat.getSeatInfo().getPrice()).isEqualTo(Price.of(8000L));
  }

  @Test
  void preempt() {
    seat.preempt();

    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.PREEMPT);
  }

  @Test
  void preemptIfUnavailable() {
    seat.preempt();

    assertThatThrownBy(() -> seat.preempt()).isInstanceOf(ReservationSeatException.class);
  }

  @Test
  void reserve() {
    seat.reserve();

    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.RESERVED);
  }

  @Test
  void reserveIfUnavailable() {
    seat.preempt();

    assertThatThrownBy(() -> seat.reserve()).isInstanceOf(ReservationSeatException.class);
  }

  @Test
  void cancel() {
    seat.cancel();

    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.AVAILABLE);
  }

  @Test
  void isReservable() {
    assertThat(seat.isReservable()).isTrue();

    seat.reserve();
    assertThat(seat.isReservable()).isFalse();

    seat.cancel();
    assertThat(seat.isReservable()).isTrue();
  }
}

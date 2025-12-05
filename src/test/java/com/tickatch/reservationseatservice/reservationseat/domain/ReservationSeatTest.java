package com.tickatch.reservationseatservice.reservationseat.domain;

import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.createReservationSeatCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.SeatInfoUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.Price;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.SeatInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationSeatTest {

  private ReservationSeat seat;

  @BeforeEach
  void setUp() {
    ReservationSeatCreateRequest request = createReservationSeatCreateRequest();

    seat = ReservationSeat.create(request);
  }

  @Test
  void create() {
    ReservationSeatCreateRequest request = createReservationSeatCreateRequest();

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

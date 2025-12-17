package com.tickatch.reservationseatservice.reservationseat.domain;

import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.createReservationSeatCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.SeatInfoUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.Price;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ReserverId;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.SeatInfo;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReservationSeatTest {

  private ReservationSeat seat;
  private UUID requestId = UUID.randomUUID();

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
    seat.preempt(requestId);

    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.PREEMPT);
    assertThat(seat.getReserverId()).isEqualTo(ReserverId.of(requestId));
  }

  @Test
  void preemptIfUnavailable() {
    seat.preempt(requestId);

    assertThatThrownBy(() -> seat.preempt(requestId)).isInstanceOf(ReservationSeatException.class);
  }

  @Test
  void reserve() {
    seat.preempt(requestId);

    seat.reserve(requestId);

    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.RESERVED);
  }

  @Test
  void reserveIfNotPreempt() {
    assertThatThrownBy(() -> seat.reserve(requestId)).isInstanceOf(ReservationSeatException.class);
  }

  @Test
  void reserveIfNotReserver() {
    seat.preempt(requestId);

    assertThatThrownBy(() -> seat.reserve(UUID.randomUUID()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void cancel() {
    seat.preempt(requestId);

    seat.cancel(requestId);

    assertThat(seat.getStatus()).isEqualTo(ReservationSeatStatus.AVAILABLE);
  }

  @Test
  void cancelIfUnavailable() {
    assertThatThrownBy(() -> seat.cancel(requestId)).isInstanceOf(ReservationSeatException.class);
  }

  @Test
  void cancelIfNotReserver() {
    seat.preempt(requestId);

    assertThatThrownBy(() -> seat.cancel(UUID.randomUUID()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void isPreemptable() {
    assertThat(seat.isPreemptable()).isTrue();

    seat.preempt(requestId);
    assertThat(seat.isPreemptable()).isFalse();

    seat.reserve(requestId);
    assertThat(seat.isPreemptable()).isFalse();

    seat.cancel(requestId);
    assertThat(seat.isPreemptable()).isTrue();
  }
}

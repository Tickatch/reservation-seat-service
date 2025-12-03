package com.tickatch.reservationseatservice.reservationseat;

import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatCreateInfo;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.test.util.ReflectionTestUtils;

public class ReservationSeatFixture {

  public static ReservationSeatsCreateRequest createReservationSeatsCreateRequest() {
    ReservationSeatCreateInfo seat1 = new ReservationSeatCreateInfo("A1", "S석", 10000L);
    ReservationSeatCreateInfo seat2 = new ReservationSeatCreateInfo("A2", "S석", 10000L);
    ReservationSeatCreateInfo seat3 = new ReservationSeatCreateInfo("B1", "A석", 5000L);

    return new ReservationSeatsCreateRequest(UUID.randomUUID(), List.of(seat1, seat2, seat3));
  }

  public static ReservationSeat createReservationSeat(ReservationSeatCreateRequest request) {
    ReservationSeat seat = ReservationSeat.create(request);

    ReflectionTestUtils.setField(seat, "id", RandomUtils.nextLong());

    return ReservationSeat.create(request);
  }

  public static ReservationSeat createReservationSeat() {
    ReservationSeat seat = ReservationSeat.create(createReservationSeatCreateRequest());

    ReflectionTestUtils.setField(seat, "id", RandomUtils.nextLong());

    return seat;
  }

  public static ReservationSeatCreateRequest createReservationSeatCreateRequest(
      UUID productId, String seatNumber, String grade, Long price) {
    return new ReservationSeatCreateRequest(productId, seatNumber, grade, price);
  }

  public static ReservationSeatCreateRequest createReservationSeatCreateRequest() {
    return createReservationSeatCreateRequest(UUID.randomUUID(), "A1", "S석", 10000L);
  }
}

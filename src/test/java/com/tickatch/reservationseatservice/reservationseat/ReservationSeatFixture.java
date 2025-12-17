package com.tickatch.reservationseatservice.reservationseat;

import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatCreateInfo;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatInfoUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.test.util.ReflectionTestUtils;

public class ReservationSeatFixture {

  public static ReservationSeatsCreateRequest createReservationSeatsCreateRequest(Long productId) {
    ReservationSeatCreateInfo seat1 = new ReservationSeatCreateInfo("A1", "S석", 10000L);
    ReservationSeatCreateInfo seat2 = new ReservationSeatCreateInfo("A2", "S석", 10000L);
    ReservationSeatCreateInfo seat3 = new ReservationSeatCreateInfo("B1", "A석", 5000L);

    return new ReservationSeatsCreateRequest(productId, List.of(seat1, seat2, seat3));
  }

  public static ReservationSeatsCreateRequest createReservationSeatsCreateRequest() {
    return createReservationSeatsCreateRequest(RandomUtils.nextLong());
  }

  public static ReservationSeat createMockReservationSeat(ReservationSeatCreateRequest request) {
    ReservationSeat seat = ReservationSeat.create(request);

    ReflectionTestUtils.setField(seat, "id", RandomUtils.nextLong());

    return ReservationSeat.create(request);
  }

  public static ReservationSeatCreateRequest createReservationSeatCreateRequest(
      Long productId, String seatNumber, String grade, Long price) {
    return new ReservationSeatCreateRequest(productId, seatNumber, grade, price);
  }

  public static ReservationSeatCreateRequest createReservationSeatCreateRequest() {
    return createReservationSeatCreateRequest(RandomUtils.nextLong(), "A1", "S석", 10000L);
  }

  public static ReservationSeatInfoUpdateRequest createReservationSeatInfoUpdateRequest(
      Long reservationSeatId, String grade, Long price) {
    return new ReservationSeatInfoUpdateRequest(reservationSeatId, grade, price);
  }

  public static ReservationSeatInfoUpdateRequest createReservationSeatInfoUpdateRequest(
      Long reservationSeatId) {
    return createReservationSeatInfoUpdateRequest(reservationSeatId, "VIP석", 20000L);
  }

  public static ReservationSeatInfosUpdateRequest createReservationSeatInfosUpdateRequest(
      List<ReservationSeatInfoUpdateRequest> updateInfos) {
    return new ReservationSeatInfosUpdateRequest(updateInfos);
  }

  public static ReservationSeatInfosUpdateRequest createReservationSeatInfosUpdateRequest(
      Long... reservationSeatIds) {
    List<ReservationSeatInfoUpdateRequest> updateInfos =
        Arrays.stream(reservationSeatIds)
            .map(ReservationSeatFixture::createReservationSeatInfoUpdateRequest)
            .toList();
    return new ReservationSeatInfosUpdateRequest(updateInfos);
  }
}

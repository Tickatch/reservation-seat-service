package com.tickatch.reservationseatservice.reservationseat.presentation.api;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatCreator;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatFinder;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.presentation.api.dto.ReservationSeatResponse;
import io.github.tickatch.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationSeatApi {

  private final ReservationSeatCreator reservationSeatCreator;
  private final ReservationSeatManager reservationSeatManager;
  private final ReservationSeatFinder reservationSeatFinder;

  @PostMapping("/api/v1/reservation-seats")
  public ApiResponse<List<ReservationSeatResponse>> create(
      @RequestBody ReservationSeatsCreateRequest createRequest) {
    List<ReservationSeat> reservationSeats = reservationSeatCreator.create(createRequest);

    List<ReservationSeatResponse> response =
        reservationSeats.stream().map(ReservationSeatResponse::from).toList();

    return ApiResponse.success(response);
  }

  @GetMapping("/api/v1/products/{productId}/reservation-seats")
  public ApiResponse<List<ReservationSeatResponse>> findAllByProductId(
      @PathVariable Long productId) {
    List<ReservationSeat> reservationSeats = reservationSeatFinder.findAllBy(productId);

    List<ReservationSeatResponse> response =
        reservationSeats.stream().map(ReservationSeatResponse::from).toList();

    return ApiResponse.success(response);
  }

  @PutMapping("/api/v1/reservation-seats")
  public ApiResponse<Void> update(@RequestBody ReservationSeatInfosUpdateRequest updateRequest) {
    reservationSeatManager.updateReservationSeatInfo(updateRequest);

    return ApiResponse.success();
  }

  @PostMapping("/api/v1/reservation-seats/{reservationSeatId}/preempt")
  public ApiResponse<Void> preempt(@PathVariable Long reservationSeatId) {
    reservationSeatManager.preempt(reservationSeatId);

    return ApiResponse.success();
  }

  @PostMapping("/api/v1/reservation-seats/{reservationSeatId}/reserve")
  public ApiResponse<Void> reserve(@PathVariable Long reservationSeatId) {
    reservationSeatManager.reserve(reservationSeatId);

    return ApiResponse.success();
  }

  @PostMapping("/api/v1/reservation-seats/{reservationSeatId}/cancel")
  public ApiResponse<Void> cancel(@PathVariable Long reservationSeatId) {
    reservationSeatManager.cancel(reservationSeatId);

    return ApiResponse.success();
  }
}

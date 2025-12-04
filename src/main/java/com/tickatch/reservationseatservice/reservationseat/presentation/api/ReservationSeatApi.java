package com.tickatch.reservationseatservice.reservationseat.presentation.api;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatCreator;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.presentation.api.dto.ReservationSeatResponse;
import io.github.tickatch.common.api.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReservationSeatApi {

  private final ReservationSeatCreator reservationSeatCreator;
  private final ReservationSeatManager reservationSeatManager;

  @PostMapping("/api/v1/reservation-seats")
  public ApiResponse<List<ReservationSeatResponse>> create(
      @RequestBody ReservationSeatsCreateRequest createRequest) {
    List<ReservationSeat> reservationSeats = reservationSeatCreator.create(createRequest);

    List<ReservationSeatResponse> response =
        reservationSeats.stream().map(ReservationSeatResponse::from).toList();

    return ApiResponse.success(response);
  }

  @PutMapping("/api/v1/reservation-seats")
  public ApiResponse<Void> update(@RequestBody ReservationSeatInfosUpdateRequest updateRequest) {
    reservationSeatManager.updateReservationSeatInfo(updateRequest);

    return ApiResponse.success();
  }
}

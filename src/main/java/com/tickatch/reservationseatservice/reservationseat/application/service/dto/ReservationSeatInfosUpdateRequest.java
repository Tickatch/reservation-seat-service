package com.tickatch.reservationseatservice.reservationseat.application.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ReservationSeatInfosUpdateRequest(
    @NotNull @Valid List<ReservationSeatInfoUpdateRequest> seatInfos) {

  public ReservationSeatInfosUpdateRequest {
    seatInfos = List.copyOf(seatInfos);
  }

  @JsonIgnore
  public List<Long> getReservationSeatIds() {
    return seatInfos.stream().map(ReservationSeatInfoUpdateRequest::reservationSeatId).toList();
  }
}

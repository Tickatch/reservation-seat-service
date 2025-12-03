package com.tickatch.reservationseatservice.reservationseat.application.service.dto;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.SeatInfoUpdateRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationSeatInfoUpdateRequest(
    @NotNull Long reservationSeatId, @NotBlank String grade, @NotNull @Min(0) Long price) {
  public SeatInfoUpdateRequest toUpdateRequest() {
    return new SeatInfoUpdateRequest(grade, price);
  }
}

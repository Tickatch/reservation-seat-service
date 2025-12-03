package com.tickatch.reservationseatservice.reservationseat.application.service.dto;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record ReservationSeatsCreateRequest(
    @NotNull UUID productId, @NotNull List<ReservationSeatCreateInfo> seatCreateInfos) {

  public ReservationSeatsCreateRequest {
    // 방어적 복사 + immutable list로 변환
    seatCreateInfos = List.copyOf(seatCreateInfos);
  }

  public List<ReservationSeatCreateRequest> toCreateRequests() {
    return this.seatCreateInfos.stream()
        .map(
            seatCreateInfo ->
                new ReservationSeatCreateRequest(
                    productId,
                    seatCreateInfo.seatNumber(),
                    seatCreateInfo.grade(),
                    seatCreateInfo.price()))
        .toList();
  }
}

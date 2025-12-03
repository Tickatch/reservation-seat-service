package com.tickatch.reservationseatservice.reservationseat.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ReservationSeatCreateRequest(
    @NotNull UUID productId,
    @NotBlank String seatNumber,
    @NotBlank String grade,
    @NotNull @Min(0) Long price) {}

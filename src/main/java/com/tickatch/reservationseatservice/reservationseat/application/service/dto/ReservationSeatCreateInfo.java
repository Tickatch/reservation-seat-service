package com.tickatch.reservationseatservice.reservationseat.application.service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationSeatCreateInfo(
    @NotBlank String seatNumber, @NotBlank String grade, @NotNull @Min(0) Long price) {}

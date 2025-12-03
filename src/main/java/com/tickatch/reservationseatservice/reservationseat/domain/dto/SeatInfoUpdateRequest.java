package com.tickatch.reservationseatservice.reservationseat.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SeatInfoUpdateRequest(@NotBlank String grade, @NotNull @Min(0) Long price) {}

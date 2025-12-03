package com.tickatch.reservationseatservice.reservationseat.domain.dto;

import java.util.UUID;

public record ReservationSeatCreateRequest(
    UUID productId, String seatNumber, String grade, Long price) {}

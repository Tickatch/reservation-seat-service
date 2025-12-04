package com.tickatch.reservationseatservice.reservationseat.domain.dto;

public record ReservationSeatCreateRequest(
    Long productId, String seatNumber, String grade, Long price) {}

package com.tickatch.reservationseatservice.reservationseat.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 예매 좌석 생성 정보.
 *
 * <p>예매 좌석 생성 시 필요한 좌석 번호, 등급, 가격 정보를 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ReservationSeatCreateInfo(
    @NotBlank String seatNumber, @NotBlank String grade, @NotNull @Min(0) Long price) {}

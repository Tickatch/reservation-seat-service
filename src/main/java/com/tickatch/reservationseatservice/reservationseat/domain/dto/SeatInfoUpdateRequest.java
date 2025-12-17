package com.tickatch.reservationseatservice.reservationseat.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 예매 좌석 정보 수정 요청.
 *
 * <p>예매 좌석의 등급 및 가격 정보를 변경하기 위한 요청 객체이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record SeatInfoUpdateRequest(@NotBlank String grade, @NotNull @Min(0) Long price) {}

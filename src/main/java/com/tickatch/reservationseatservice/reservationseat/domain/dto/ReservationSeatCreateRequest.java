package com.tickatch.reservationseatservice.reservationseat.domain.dto;

/**
 * 단일 예매 좌석 생성 요청.
 *
 * <p>상품에 속한 한 개의 예매 좌석 정보를 생성하기 위한 요청 객체이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public record ReservationSeatCreateRequest(
    Long productId, String seatNumber, String grade, Long price) {}

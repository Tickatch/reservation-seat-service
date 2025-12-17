package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.tickatch.common.event.DomainEvent;
import java.time.Instant;
import lombok.Getter;

/**
 * 상품 삭제 시 예매 좌석 삭제 요청 이벤트.
 *
 * <p>상품 삭제 도메인 이벤트가 발생하면 해당 상품에 대한 예매 좌석 정보를 제거하기 위해 생성되어 전달되는 메시지이다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Getter
public class ProductReservationSeatDeleteRequest extends DomainEvent {

  private final Long productId;

  /**
   * 상품 ID 기반의 예매 좌석 삭제 요청 이벤트를 생성한다.
   *
   * @param productId 삭제 대상 상품 ID
   */
  public ProductReservationSeatDeleteRequest(Long productId) {
    super();
    this.productId = productId;
  }

  /**
   * JSON 역직렬화를 위한 생성자.
   *
   * <p>이벤트 정보와 함께 상품 ID를 전달받아 예매 좌석 삭제 요청 이벤트를 복원한다.
   *
   * @param eventId 이벤트 ID
   * @param occurredAt 이벤트 발생 시각
   * @param version 이벤트 버전
   * @param productId 삭제 대상 상품 ID
   */
  @JsonCreator
  public ProductReservationSeatDeleteRequest(
      @JsonProperty("eventId") String eventId,
      @JsonProperty("occurredAt") Instant occurredAt,
      @JsonProperty("version") int version,
      @JsonProperty("productId") Long productId) {
    super(eventId, occurredAt, version);
    this.productId = productId;
  }
}

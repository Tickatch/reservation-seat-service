package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.tickatch.common.event.DomainEvent;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ProductReservationSeatDeleteRequest extends DomainEvent {
  private final Long productId;

  public ProductReservationSeatDeleteRequest(Long productId) {
    super();
    this.productId = productId;
  }

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

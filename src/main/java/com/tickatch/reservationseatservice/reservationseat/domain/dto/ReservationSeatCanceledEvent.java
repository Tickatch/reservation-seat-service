package com.tickatch.reservationseatservice.reservationseat.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.tickatch.common.event.DomainEvent;
import java.time.Instant;
import lombok.Getter;

@Getter
public class ReservationSeatCanceledEvent extends DomainEvent {
  private static final String AGGREGATE_TYPE = "ReservationSeat";
  private static final String ROUTING_KEY = "reservation-seat.canceled.product";

  private final Long productId;
  private final String grade;
  private final Integer count;

  public ReservationSeatCanceledEvent(Long productId, String grade) {
    super();
    this.productId = productId;
    this.grade = grade;
    this.count = 1;
  }

  @JsonCreator
  public ReservationSeatCanceledEvent(
      @JsonProperty("eventId") String eventId,
      @JsonProperty("occurredAt") Instant occurredAt,
      @JsonProperty("version") int version,
      @JsonProperty("productId") Long productId,
      @JsonProperty("grade") String grade,
      @JsonProperty("count") Integer count) {
    super(eventId, occurredAt, version);
    this.productId = productId;
    this.grade = grade;
    this.count = count;
  }

  @Override
  public String getAggregateType() {
    return AGGREGATE_TYPE;
  }

  @Override
  public String getRoutingKey() {
    return ROUTING_KEY;
  }
}

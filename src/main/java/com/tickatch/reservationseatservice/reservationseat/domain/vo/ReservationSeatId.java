package com.tickatch.reservationseatservice.reservationseat.domain.vo;

import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReservationSeatId {
  private final Long id;

  public static ReservationSeatId of(Long id) {
    return new ReservationSeatId(Objects.requireNonNull(id));
  }

  public Long id() {
    return this.id;
  }
}

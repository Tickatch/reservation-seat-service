package com.tickatch.reservationseatservice.reservationseat.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReserverId {
  @Column(name = "reserver_id")
  private UUID id;

  public static ReserverId of(UUID id) {
    return new ReserverId(Objects.requireNonNull(id));
  }

  public UUID toUuid() {
    return this.id;
  }

  @Override
  public String toString() {
    return this.id.toString();
  }
}

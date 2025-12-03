package com.tickatch.reservationseatservice.reservationseat.domain;

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
public class ProductId {
  @Column(name = "product_id", nullable = false)
  private UUID id;

  public static ProductId of(UUID id) {
    return new ProductId(Objects.requireNonNull(id));
  }

  public UUID toUuid() {
    return id;
  }

  @Override
  public String toString() {
    return id.toString();
  }
}

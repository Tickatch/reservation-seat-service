package com.tickatch.reservationseatservice.reservationseat.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
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
  private Long id;

  public static ProductId of(Long id) {
    return new ProductId(Objects.requireNonNull(id));
  }

  public Long id() {
    return this.id;
  }
}

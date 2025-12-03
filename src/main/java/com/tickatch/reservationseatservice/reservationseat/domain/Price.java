package com.tickatch.reservationseatservice.reservationseat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Price {
  public static final Price ZERO = new Price(0L);

  @Column(name = "price", nullable = false)
  private Long value;

  public static Price of(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("value 값은 0 이상이여야 합니다.");
    }
    return new Price(value);
  }

  public Price add(Price other) {
    return new Price(this.value + other.value);
  }

  public Price multiply(int multiplier) {
    return new Price(this.value * multiplier);
  }

  public long value() {
    return this.value;
  }
}

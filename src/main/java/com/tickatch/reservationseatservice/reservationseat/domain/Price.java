package com.tickatch.reservationseatservice.reservationseat.domain;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Price {

  public static final Price ZERO = new Price(0);

  private final Long value;

  private Price(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("value 값은 0 이상이여야 합니다.");
    }
    this.value = value;
  }

  public static Price of(long value) {
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

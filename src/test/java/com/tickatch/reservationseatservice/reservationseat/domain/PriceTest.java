package com.tickatch.reservationseatservice.reservationseat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PriceTest {

  @Test
  void of() {
    Price price = Price.of(10);

    assertThat(price.value()).isEqualTo(10);
  }

  @Test
  void ofValueIfUnderZero() {
    assertThatThrownBy(() -> Price.of(-1)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void add() {
    Price price = Price.of(10);

    Price result = price.add(Price.of(20));

    assertThat(result.value()).isEqualTo(30);
  }

  @Test
  void multiply() {
    Price price = Price.of(10);

    Price result = price.multiply(3);

    assertThat(result.value()).isEqualTo(30);
  }
}

package com.tickatch.reservationseatservice.reservationseat.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SeatInfoTest {
  @Test
  void of() {
    SeatInfo seat = SeatInfo.of("A1", "S석", Price.of(10000));

    assertThat(seat.getSeatNumber()).isEqualTo("A1");
    assertThat(seat.getGrade()).isEqualTo("S석");
    assertThat(seat.getPrice()).isEqualTo(Price.of(10000));
  }

  @Test
  void ofIfNullGarde() {
    assertThatThrownBy(() -> SeatInfo.of("A1", null, Price.of(10000)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void ofIfBlankGarde() {
    assertThatThrownBy(() -> SeatInfo.of("A1", "  ", Price.of(10000)))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void ofIfNullPrice() {
    assertThatThrownBy(() -> SeatInfo.of("A1", "S석", null))
        .isInstanceOf(IllegalArgumentException.class);
  }
}

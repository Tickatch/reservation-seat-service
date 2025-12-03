package com.tickatch.reservationseatservice.reservationseat.domain;

import static org.springframework.util.Assert.*;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeatInfo {
  @Column(nullable = false)
  private String seatNumber;

  @Column(nullable = false)
  private String grade;

  @Embedded private Price price;

  public static SeatInfo of(String seatNumber, String grade, Price price) {
    hasText(grade, "좌석 등급은 필수값입니다.");
    notNull(price, "가격은 필수값입니다.");

    return new SeatInfo(Objects.requireNonNull(seatNumber), grade, price);
  }
}
